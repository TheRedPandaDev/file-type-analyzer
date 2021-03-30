# File Type Analyzer

### Usage
    java Main DIRECTORY_WITH_FILES PATTERN_DATABASE

### Examples
    java Main test_files patterns.db
    doc_0.doc: MS Office Word 2003
    doc_1.pptx: MS Office PowerPoint 2007+
    doc_2.pdf: PDF document
    file.pem: PEM certificate

#### patterns.db contents:
    1;"%PDF-";"PDF document"
    2;"pmview";"PCP pmview config"
    4;"PK";"Zip archive"
    5;"vnd.oasis.opendocument.presentation";"OpenDocument presentation"
    6;"W.o.r.d";"MS Office Word 2003"
    6;"P.o.w.e.r.P.o.i";"MS Office PowerPoint 2003"
    7;"word/_rels";"MS Office Word 2007+"
    7;"ppt/_rels";"MS Office PowerPoint 2007+"
    7;"xl/_rels";"MS Office Excel 2007+"
    8;"-----BEGIN\ CERTIFICATE-----";"PEM certificate"
    9;"ftypjp2";"ISO Media JPEG 2000"
    9;"ftypiso2";"ISO Media MP4 Base Media v2"
