### 命令执行方式
python3 decode_mars_nocrypt_log_file.py xxx.xlog


### 文件描述
1. 未加密解析：执行 decode_mars_nocrypt_log_file.py
2. 加密解析：执行 decode_mars_crypt_log_file.py
3. 加密方式：执行 gen_key.py， 获取一组随机的公钥、私钥。传入Xlog.open 里面的 pubKey


### 其他
 源解密文件在：https://github.com/Tencent/mars/blob/master/mars/xlog/crypt/decode_mars_nocrypt_log_file.py
 因为是python2版本，所以找到下面兼容python3版本
 1. 文件来源：https://github.com/Tencent/mars/issues/804
 2. xxx2.py 只是另一种兼容python3的写法，来源：https://dnzp.com/jy/202303/104079.html
