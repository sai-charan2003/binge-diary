import os
import re

# We will recursively find all .kt files
project_root = '/Users/saicharan/Developer/CrossPlatform/binge-diary'

# These are the top level packages we want to prepend with com.charan.bingediary
# We also include com.bingediary to replace it with com.charan.bingediary
# and the ones without a package will just get com.charan.bingediary
prefixes_to_update = ['data.', 'di.', 'presentation.', 'utils.', 'com.bingediary.']

def process_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    lines = content.split('\n')
    new_lines = []
    
    has_package = False
    
    for i, line in enumerate(lines):
        # Handle package declaration
        if line.startswith('package '):
            has_package = True
            current_pkg = line[len('package '):].strip()
            if current_pkg.startswith('com.charan.bingediary'):
                new_lines.append(line)
            elif current_pkg.startswith('com.bingediary.'):
                new_lines.append(line.replace('com.bingediary.', 'com.charan.bingediary.'))
            else:
                new_lines.append(f'package com.charan.bingediary.{current_pkg}')
            continue
            
        # Handle imports
        if line.startswith('import '):
            imported = line[len('import '):].strip()
            # Special case: import Screen
            if imported == 'Screen':
                new_lines.append('import com.charan.bingediary.Screen')
                continue
            
            updated = False
            for prefix in prefixes_to_update:
                if imported.startswith(prefix):
                    if prefix == 'com.bingediary.':
                        new_lines.append(line.replace('com.bingediary.', 'com.charan.bingediary.'))
                    else:
                        new_lines.append(f'import com.charan.bingediary.{imported}')
                    updated = True
                    break
            
            if not updated:
                new_lines.append(line)
            continue
            
        new_lines.append(line)

    if not has_package:
        # Add package to the top
        # Find the first non-empty line that is not a comment to insert the package before it
        insert_idx = 0
        for i, line in enumerate(new_lines):
            if line.strip() and not line.strip().startswith('//'):
                insert_idx = i
                break
        
        new_lines.insert(insert_idx, 'package com.charan.bingediary')
        new_lines.insert(insert_idx + 1, '') # empty line

    # Write back
    with open(filepath, 'w') as f:
        f.write('\n'.join(new_lines))

for root, dirs, files in os.walk(project_root):
    if 'build' in root or '.idea' in root or '.fleet' in root or '.gradle' in root or '.git' in root or '.gemini' in root:
        continue
    for file in files:
        if file.endswith('.kt'):
            process_file(os.path.join(root, file))

print("Done updating packages and imports.")
