import codecs

with open('build_log_v4.txt', 'r', encoding='utf-16le', errors='replace') as f:
    for line in f:
        if 'e: file:///' in line or 'error:' in line.lower() or 'unresolved reference' in line.lower() or 'exception' in line.lower() or 'failure' in line.lower():
            print(line.strip())
