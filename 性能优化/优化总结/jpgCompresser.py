# 执行命令如下：
# pngPath=tools/png/jpgCompresser.py
# if [ -f "$pngPath" ]; then
#     python3 tools/png/jpgCompresser.py
# fi
# --------------------------------------

import os
import subprocess
import time

# JPG 压缩质量
quality = 80

# 遍历所有文件获取 JPG 图片
jpg_files = []
for root, dirs, files in os.walk('.'):
  for file in files:
    if file.endswith('.jpg'):
      jpg_files.append(os.path.join(root, file))

for jpg_file in jpg_files:

  # 检查 PNG 文件是否存在,不存在则跳过
  if not os.path.exists(jpg_file):
    print(f'{jpg_file} 文件不存在,跳过!')
    continue

  # 获取 PNG 图片文件大小
  file_size = os.path.getsize(jpg_file)

  # 如果文件大小小于 5kb,跳过
  if file_size < 5120:
    print(f'{jpg_file} 文件小于 5kb,跳过!')
    continue

  # 如果已经压缩过则跳过
  compress_path = jpg_file.replace('.jpg', '_compress.jpg')
  if os.path.exists(compress_path):
    print(f'{jpg_file} 已压缩,跳过!')
    continue

  # 压缩 JPG 图片
  cmd = f'jpg_compress {quality} {jpg_file} {compress_path}'
  subprocess.run(cmd, shell=True)

#   time.sleep(1)
#
#   if not os.path.exists(compress_path):
#     print(f'{compress_path} 文件不存在,跳过!')
#     continue
#
#   # 获取压缩后文件大小,如果大于原文件大小则跳过
#   compress_size = os.path.getsize(compress_path)
#   file_size = os.path.getsize(jpg_file)
#   if compress_size > file_size:
#     print(f'{jpg_file} 压缩后大于原文件,跳过!')
#     os.remove(compress_path)
#     continue
#
#   # 将压缩后的文件重命名为原文件
#   os.remove(jpg_file)
#   os.rename(compress_path, jpg_file)

  print(f'成功压缩 {jpg_file} !')