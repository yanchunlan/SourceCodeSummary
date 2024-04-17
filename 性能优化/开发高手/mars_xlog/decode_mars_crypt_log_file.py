import binascii
import glob
import os
import struct
import sys
import traceback
import zlib

import pyelliptic
import zstandard as zstd

MAGIC_NO_COMPRESS_START = 0x03
MAGIC_NO_COMPRESS_START1 = 0x06
MAGIC_NO_COMPRESS_NO_CRYPT_START = 0x08
MAGIC_COMPRESS_START = 0x04
MAGIC_COMPRESS_START1 = 0x05
MAGIC_COMPRESS_START2 = 0x07
MAGIC_COMPRESS_NO_CRYPT_START = 0x09

MAGIC_SYNC_ZSTD_START = 0x0A
MAGIC_SYNC_NO_CRYPT_ZSTD_START = 0x0B
MAGIC_ASYNC_ZSTD_START = 0x0C
MAGIC_ASYNC_NO_CRYPT_ZSTD_START = 0x0D

MAGIC_END = 0x00

lastseq = 0

PRIV_KEY = "xxx"
PUB_KEY = "xxxxxx"


class ZstdDecompressReader:
    def __init__(self, buffer):
        self.buffer = buffer

    def read(self, size):
        return self.buffer


def tea_decipher(v, k):
    op = 0xffffffff
    v0, v1 = struct.unpack('=LL', v[0:8])
    k1, k2, k3, k4 = struct.unpack('=LLLL', k[0:16])
    delta = 0x9E3779B9
    s = (delta << 4) & op
    for i in range(16):
        v1 = (v1 - (((v0 << 4) + k3) ^ (v0 + s) ^ ((v0 >> 5) + k4))) & op
        v0 = (v0 - (((v1 << 4) + k1) ^ (v1 + s) ^ ((v1 >> 5) + k2))) & op
        s = (s - delta) & op
    return struct.pack('=LL', v0, v1)


def tea_decrypt(v, k):
    num = int(len(v) / 8) * 8
    ret = b''
    for i in range(0, num, 8):
        x = tea_decipher(v[i:i + 8], k)
        ret = ret + x

    ret = ret + v[num:]
    return ret


def IsGoodLogBuffer(_buffer, _offset, count):
    if _offset == len(_buffer): return (True, '')

    magic_start = _buffer[_offset]
    if MAGIC_NO_COMPRESS_START == magic_start or MAGIC_COMPRESS_START == magic_start or MAGIC_COMPRESS_START1 == magic_start:
        crypt_key_len = 4
    elif MAGIC_COMPRESS_START2 == magic_start or MAGIC_NO_COMPRESS_START1 == magic_start or MAGIC_NO_COMPRESS_NO_CRYPT_START == magic_start or MAGIC_COMPRESS_NO_CRYPT_START == magic_start \
            or MAGIC_SYNC_ZSTD_START == magic_start or MAGIC_SYNC_NO_CRYPT_ZSTD_START == magic_start or MAGIC_ASYNC_ZSTD_START == magic_start or MAGIC_ASYNC_NO_CRYPT_ZSTD_START == magic_start:
        crypt_key_len = 64
    else:
        return (False, '_buffer[%d]:%d != MAGIC_NUM_START' % (_offset, _buffer[_offset]))

    headerLen = 1 + 2 + 1 + 1 + 4 + crypt_key_len

    if _offset + headerLen + 1 + 1 > len(_buffer): return (
        False, 'offset:%d > len(buffer):%d' % (_offset, len(_buffer)))
    length = struct.unpack_from("I", buffer(_buffer, _offset + headerLen - 4 - crypt_key_len, 4))[0]
    if _offset + headerLen + length + 1 > len(_buffer): return (
        False, 'log length:%d, end pos %d > len(buffer):%d' % (length, _offset + headerLen + length + 1, len(_buffer)))
    if MAGIC_END != _buffer[_offset + headerLen + length]: return (False,
                                                                   'log length:%d, buffer[%d]:%d != MAGIC_END' % (
                                                                       length, _offset + headerLen + length,
                                                                       _buffer[_offset + headerLen + length]))

    if (1 >= count):
        return (True, '')
    else:
        return IsGoodLogBuffer(_buffer, _offset + headerLen + length + 1, count - 1)


def GetLogStartPos(_buffer, _count):
    offset = 0
    while True:
        if offset >= len(_buffer): break

        if MAGIC_NO_COMPRESS_START == _buffer[offset] or MAGIC_NO_COMPRESS_START1 == _buffer[
            offset] or MAGIC_COMPRESS_START == _buffer[offset] or MAGIC_COMPRESS_START1 == _buffer[
            offset] or MAGIC_COMPRESS_START2 == _buffer[offset] or MAGIC_COMPRESS_NO_CRYPT_START == _buffer[
            offset] or MAGIC_NO_COMPRESS_NO_CRYPT_START == _buffer[offset] \
                or MAGIC_SYNC_ZSTD_START == _buffer[offset] or MAGIC_SYNC_NO_CRYPT_ZSTD_START == _buffer[
            offset] or MAGIC_ASYNC_ZSTD_START == _buffer[offset] or MAGIC_ASYNC_NO_CRYPT_ZSTD_START == _buffer[offset]:
            if IsGoodLogBuffer(_buffer, offset, _count)[0]: return offset
        offset += 1

    return -1


def DecodeBuffer(_buffer, _offset, _outbuffer):
    if _offset >= len(_buffer): return -1
    ret = IsGoodLogBuffer(_buffer, _offset, 1)
    if not ret[0]:
        fixpos = GetLogStartPos(_buffer[_offset:], 1)
        if -1 == fixpos:
            return -1
        else:
            _outbuffer.extend("[F]decode_log_file.py decode error len=%d, result:%s \n" % (fixpos, ret[1]))
            _offset += fixpos

    magic_start = _buffer[_offset]
    if MAGIC_NO_COMPRESS_START == magic_start or MAGIC_COMPRESS_START == magic_start or MAGIC_COMPRESS_START1 == magic_start:
        crypt_key_len = 4
    elif MAGIC_COMPRESS_START2 == magic_start or MAGIC_NO_COMPRESS_START1 == magic_start or MAGIC_NO_COMPRESS_NO_CRYPT_START == magic_start or MAGIC_COMPRESS_NO_CRYPT_START == magic_start \
            or MAGIC_SYNC_ZSTD_START == magic_start or MAGIC_SYNC_NO_CRYPT_ZSTD_START == magic_start or MAGIC_ASYNC_ZSTD_START == magic_start or MAGIC_ASYNC_NO_CRYPT_ZSTD_START == magic_start:
        crypt_key_len = 64
    else:
        _outbuffer.extend('in DecodeBuffer _buffer[%d]:%d != MAGIC_NUM_START' % (_offset, magic_start))
        return -1

    headerLen = 1 + 2 + 1 + 1 + 4 + crypt_key_len
    length = struct.unpack_from("I", buffer(_buffer, _offset + headerLen - 4 - crypt_key_len, 4))[0]
    tmpbuffer = bytearray(length)

    seq = struct.unpack_from("H", buffer(_buffer, _offset + headerLen - 4 - crypt_key_len - 2 - 2, 2))[0]
    begin_hour = struct.unpack_from("c", buffer(_buffer, _offset + headerLen - 4 - crypt_key_len - 1 - 1, 1))[0]
    end_hour = struct.unpack_from("c", buffer(_buffer, _offset + headerLen - 4 - crypt_key_len - 1, 1))[0]

    global lastseq
    if seq != 0 and seq != 1 and lastseq != 0 and seq != (lastseq + 1):
        _outbuffer.extend("[F]decode_log_file.py log seq:%d-%d is missing\n" % (lastseq + 1, seq - 1))

    if seq != 0:
        lastseq = seq

    tmpbuffer[:] = _buffer[_offset + headerLen:_offset + headerLen + length]

    try:

        if MAGIC_NO_COMPRESS_START1 == _buffer[_offset] or MAGIC_SYNC_ZSTD_START == _buffer[_offset]:
            pass

        elif MAGIC_COMPRESS_START2 == _buffer[_offset] or MAGIC_ASYNC_ZSTD_START == _buffer[_offset]:
            svr = pyelliptic.ECC(curve='secp256k1')
            client = pyelliptic.ECC(curve='secp256k1')
            client.pubkey_x = buffer(_buffer, _offset + headerLen - crypt_key_len, int(crypt_key_len / 2))
            client.pubkey_y = buffer(_buffer, int(_offset + headerLen - crypt_key_len / 2), int(crypt_key_len / 2))

            svr.privkey = binascii.unhexlify(PRIV_KEY)
            tea_key = svr.get_ecdh_key(client.get_pubkey())

            tmpbuffer = tea_decrypt(tmpbuffer, tea_key)
            if MAGIC_COMPRESS_START2 == _buffer[_offset]:
                decompressor = zlib.decompressobj(-zlib.MAX_WBITS)
                tmpbuffer = decompressor.decompress(bytes(tmpbuffer))
            else:
                decompressor = zstd.ZstdDecompressor()
                tmpbuffer = next(decompressor.read_from(ZstdDecompressReader(tmpbuffer), 100000, 1000000))
        elif MAGIC_ASYNC_NO_CRYPT_ZSTD_START == _buffer[_offset]:
            decompressor = zstd.ZstdDecompressor()
            tmpbuffer = next(decompressor.read_from(ZstdDecompressReader(bytes(tmpbuffer)), 100000, 1000000))
        elif MAGIC_COMPRESS_START == _buffer[_offset] or MAGIC_COMPRESS_NO_CRYPT_START == _buffer[_offset]:
            decompressor = zlib.decompressobj(-zlib.MAX_WBITS)
            tmpbuffer = decompressor.decompress(bytes(tmpbuffer))
        elif MAGIC_COMPRESS_START1 == _buffer[_offset]:
            decompress_data = bytearray()
            while len(tmpbuffer) > 0:
                single_log_len = struct.unpack_from("H", buffer(tmpbuffer, 0, 2))[0]
                decompress_data.extend(tmpbuffer[2:single_log_len + 2])
                tmpbuffer[:] = tmpbuffer[single_log_len + 2:len(tmpbuffer)]

            decompressor = zlib.decompressobj(-zlib.MAX_WBITS)
            tmpbuffer = decompressor.decompress(bytes(decompress_data))

        else:
            pass
    except Exception as e:
        traceback.print_exc()
        _outbuffer.extend("[F]decode_log_file.py decompress err, " + "\n")
        return _offset + headerLen + length + 1

    _outbuffer.extend(tmpbuffer)

    return _offset + headerLen + length + 1


def buffer(_buffer, _offset, _size):
    mv = memoryview(_buffer[_offset: _offset + _size])
    return mv.tobytes()


def ParseFile(_file, _outfile):
    fp = open(_file, "rb")
    _buffer = bytearray(os.path.getsize(_file))
    fp.readinto(_buffer)
    fp.close()
    startpos = GetLogStartPos(_buffer, 2)
    if -1 == startpos:
        return False

    outbuffer = bytearray()

    while True:
        startpos = DecodeBuffer(_buffer, startpos, outbuffer)
        if -1 == startpos: break;

    if 0 == len(outbuffer): return False

    fpout = open(_outfile, "wb")
    fpout.write(outbuffer)
    fpout.close()
    return True


def main(args):
    global lastseq

    if 1 == len(args):
        if os.path.isdir(args[0]):
            filelist = glob.glob(args[0] + "/*.xlog")
            for filepath in filelist:
                lastseq = 0
                ParseFile(filepath, filepath + ".log")
        else:
            ParseFile(args[0], args[0] + ".log")
    elif 2 == len(args):
        ParseFile(args[0], args[1])
    else:
        filelist = glob.glob("*.xlog")
        for filepath in filelist:
            lastseq = 0
            ParseFile(filepath, filepath + ".log")


if __name__ == "__main__":
    main(sys.argv[1:])
