import codecs
import re
with codecs.open('build_log_v4.txt', 'r', 'utf-16le') as f:
    content = f.read()
    matches = re.findall(r'(e:.*?)(?=e:.*?$|\Z)', content, re.DOTALL | re.MULTILINE)
    for m in matches:
        print(m.strip()[:200])
