# File Type Analyzer

### Usage
    java Main DIRECTORY_WITH_FILES PATTERN FILE_TYPE_NAME

### Examples
    java Main test_files "-----BEGIN\ CERTIFICATE-----" "PEM certificate"
    file.pem: PEM certificate
    doc_1.docx: Unknown file type
    doc_2.pdf: Unknown file type
    
    java Main test_files "%PDF-" "PDF document"
    file.pem: Unknown file type
    doc_1.docx: Unknown file type
    doc_2.pdf: PDF document
