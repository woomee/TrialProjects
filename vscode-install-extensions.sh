#!/bin/bash

# Package list
pkglist=(
    # Brackage, Indent
    coenraads.bracket-pair-colorizer-2
    oderwat.indent-rainbow
    davidhouchin.whitespace-plus
    mosapride.zenkaku
    # Draw
    hediet.vscode-drawio
    # Markdown
    yzane.markdown-pdf
    shd101wyy.markdown-preview-enhanced
    # Docker
    ms-azuretools.vscode-docker
)

for i in ${pkglist[@]}; do
    code --install-extension $i
done
