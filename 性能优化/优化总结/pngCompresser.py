# 执行命令如下：
# pngPath=tools/png/pngCompresser.py
# if [ -f "$pngPath" ]; then
#     python3 tools/png/pngCompresser.py
# fi
# --------------------------------------

# 环境配置如下
# pip install Pillow
# export PYTHONPATH=$PYTHONPATH:/usr/local/lib/python3.8/site-packages
# source ~/.bash_profile

import os
import glob
import time
import PIL
from PIL import Image

# PNG 图片目录
png_dir = 'banban_base'

# 获取目录及子目录下所有 PNG 图片
png_files = glob.glob(os.path.join(png_dir, '**/*.png'), recursive=True)

# 需要跳过的文件
skip_files = ['assets/images/home_entry_bg_under_compress.png']

# PNGQUANT 命令
cmd = 'pngquant --ext .png --force {}'

# 逐个压缩 PNG 图片
for png_file in png_files:
# 检查 PNG 文件是否在跳过列表,如果在则跳过
  if os.path.basename(png_file) in skip_files:
#     print(f'{png_file} 在跳过列表,跳过!')
    continue

# 检查 PNG 文件是否存在,不存在则跳过
  if not os.path.exists(png_file):
#     print(f'{png_file} 文件不存在,跳过!')
    continue


  # 获取 PNG 图片文件大小
  file_size = os.path.getsize(png_file)

  # 如果文件大小小于 5kb,跳过
  if file_size < 5120:
#     print(f'{png_file} 文件小于 5kb,跳过!')
    continue

  # 打开 PNG 图片获取颜色模式
  image = Image.open(png_file)   # 修改为与上一行代码相同的缩进级别
  color_mode = image.mode

  # 如果是 8 位色彩模式,跳过
  if color_mode == 'P':
#     print(f'{png_file} 是 8 位色彩模式,跳过!')
    continue

  file_cmd = cmd.format(png_file)

# 检查 PNGQUANT 命令是否可以执行
  if os.system(f'pngquant -h') != 0:
    print(f'{png_file} PNGQUANT 命令无法执行,跳过!')
    continue

  # 执行 PNGQUANT 命令,错误跳过
  try:
    # 执行 PNGQUANT 命令
    process = os.popen(file_cmd)
    # 等待 2 秒,给 PNGQUANT 进程一定时间生成输出结果
#     time.sleep(2)
    # 读取结果
    result = process.read()
#     result = os.popen(file_cmd).read()
  except Exception as e:
#     print(f'{png_file} 压缩异常,跳过!错误信息:{e}')
    continue

 # 检查结果是否为空,如果为空则跳过
  if not result:
#     print(f'{png_file} 读取 PNGQUANT 结果为空,跳过!')
    continue

  # 从结果获取压缩文件大小
  size_str = result.strip().split(' ')[0]
  size = int(size_str)

  # 获取原文件大小
  orig_size = os.path.getsize(png_file) - 2048

  # 如果压缩后大于原文件,跳过 （仅大于2KB则压缩）
  if size > orig_size:
#     print(f'{png_file} 压缩后大于原文件,跳过!')
    continue

  print(f'PNG压缩成功 {png_file} !')

  # 覆盖原文件
  os.system(file_cmd)
#
# # 计算总压缩信息
# total_size = 0
# total_compress_size = 0
# for png_file in png_files:
#   total_size += os.path.getsize(png_file)
#   try:
#       compress_png = png_file.replace('.png', '_compress.png')
#       total_compress_size += os.path.getsize(compress_png)
#   except Exception:
#       print(f'{png_file} compress_png 变量不存在,跳过!')
#       continue
# # 计算压缩率
# if total_compress_size == 0:
#   print('未压缩任何 PNG 图片,跳过压缩率计算!')
# else:
#   rate = total_size / total_compress_size
#   print(f'压缩率: {rate * 100:.2f}%')
#
# # 输出压缩信息文件
# with open('compress_info.txt', 'w') as f:
#   for png_file in png_files:
#     try:
#         compress_png = png_file.replace('.png', '_compress.png')
#         size = os.path.getsize(png_file)
#         compress_size = os.path.getsize(compress_png)
#     except Exception:
#         print(f'{png_file} compress_png 变量不存在,跳过!')
#         continue
#     f.write(f'{png_file} {size} {compress_png} {compress_size}\n')
#   f.write(f'总压缩前大小:{total_size}  总压缩后大小:{total_compress_size}  总压缩率:{rate}')

print('PNG 图片压缩完成!')