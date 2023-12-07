package com.jef.file;


import com.aspose.words.Document;
import com.aspose.words.MarkdownSaveOptions;

/**
 * @author tufujie
 * @date 2023/11/28
 */
public class WordToMarkdown {

    public static void main(String[] args) throws Exception {
        String dataDir = "E:\\Desktop\\test\\";
        // Open the document.
        Document doc = new Document(dataDir + "Debug回到某一行代码.docx");
        MarkdownSaveOptions markdownSaveOptions = new MarkdownSaveOptions();
        markdownSaveOptions.setImagesFolder("./media");
        doc.save(dataDir + "Debug回到某一行代码.md", markdownSaveOptions);

    }

}