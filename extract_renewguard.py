#!/usr/bin/env python3
"""
Extrait tous les fichiers de code source (Java, XML, FXML, CSS, pom.xml)
contenus dans RenewGuard_AI_Dossier_Technique.docx et les écrit dans une
arborescence Maven prête à compiler.

Usage:
    # nécessite pandoc installé (apt install pandoc / brew install pandoc)
    python3 extract_renewguard.py chemin/vers/RenewGuard_AI_Dossier_Technique.docx dossier_sortie/

Le document Word contient une section par fichier, sous la forme :
    **src/main/java/fr/renewguard/.../MonFichier.java**
    > ligne de code 1
    > ligne de code 2
    ...
Ce script repère chaque en-tête de fichier, extrait le bloc de citation
qui suit, corrige les échappements Markdown introduits par la conversion
docx -> markdown (\\@, \\$, \\+, \\<, \\>, \\", etc.) et écrit le fichier
au bon chemin.
"""
import re
import os
import subprocess
import sys


def convert_docx_to_markdown(docx_path: str, out_dir: str) -> str:
    os.makedirs(out_dir, exist_ok=True)
    md_path = os.path.join(out_dir, "_dossier_converted.md")
    subprocess.run(
        ["pandoc", "-t", "markdown", "--wrap=none", docx_path, "-o", md_path],
        check=True,
    )
    with open(md_path, encoding="utf-8") as f:
        return f.read()


HEADING_RE = re.compile(r"^\*\*(.+?)\*\*$")


def is_file_heading(text: str) -> bool:
    if text == "pom.xml":
        return True
    if "/" not in text:
        return False
    return text.endswith((".java", ".xml", ".fxml", ".css", ".properties"))


def unescape(s: str) -> str:
    s = s.replace("\\@", "@").replace("\\_", "_")
    s = s.replace("\\[", "[").replace("\\]", "]")
    s = s.replace("\\{", "{").replace("\\}", "}")
    s = s.replace("\\<", "<").replace("\\>", ">")
    s = s.replace('\\"', '"').replace("\\'", "'")
    s = s.replace("\\`", "`").replace("\\|", "|")
    s = s.replace("\\*", "*").replace("\\-", "-")
    s = s.replace("\\.", ".").replace("\\#", "#")
    s = s.replace("\\$", "$").replace("\\+", "+")
    s = s.replace("\\\\", "\\")  # doit rester en dernier
    return s


def extract(markdown_text: str, out_dir: str):
    lines = markdown_text.splitlines(keepends=True)
    headings = []
    for i, line in enumerate(lines):
        m = HEADING_RE.match(line.strip())
        if m and is_file_heading(m.group(1).strip()):
            headings.append((i, m.group(1).strip()))

    print(f"{len(headings)} fichiers trouves dans le document.")

    for idx, (line_idx, path) in enumerate(headings):
        start = line_idx + 1
        end = headings[idx + 1][0] if idx + 1 < len(headings) else len(lines)
        block = lines[start:end]

        code_lines = []
        for raw in block:
            stripped = raw.rstrip("\n").strip()
            if stripped.startswith("> "):
                code_lines.append(unescape(stripped[2:]))
            elif stripped == ">":
                code_lines.append("")

        while code_lines and code_lines[0] == "":
            code_lines.pop(0)
        while code_lines and code_lines[-1] == "":
            code_lines.pop()

        content = "\n".join(code_lines) + "\n"

        full_path = os.path.join(out_dir, path)
        os.makedirs(os.path.dirname(full_path), exist_ok=True)
        with open(full_path, "w", encoding="utf-8") as f:
            f.write(content)
        print(f"  ecrit : {path}")

    print(f"\nTermine. {len(headings)} fichiers ecrits dans {out_dir}/")


if __name__ == "__main__":
    if len(sys.argv) != 3:
        print(__doc__)
        sys.exit(1)
    docx_path, out_dir = sys.argv[1], sys.argv[2]
    md = convert_docx_to_markdown(docx_path, out_dir)
    extract(md, out_dir)
