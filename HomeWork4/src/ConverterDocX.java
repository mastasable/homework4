import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by admin on 16.03.2015.
 */
public class ConverterDocX {
    public static void main(String args[]) throws IOException, ParserConfigurationException, SAXException {
        System.out.println("Укажите путь к файлу");
        Scanner inputStream = new Scanner(System.in);
        final String zipFile = inputStream.nextLine();
        String txtFile = zipFile.substring(0, zipFile.length() - 5) + ".txt";
        File outputFile = new File(txtFile);

        try (final ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile));
             final PrintWriter out = new PrintWriter(outputFile.getAbsoluteFile()))
             {
                 ZipEntry zipEntry = null;
                 while(true){
                     zipEntry = zipInputStream.getNextEntry();
                     if (zipEntry == null){
                         System.out.println("Ошибка в файле .docx");
                         return;
                     }
                     if (zipEntry.getName().equals("word/document.xml"))
                         break;
                 }
                 SAXParserFactory factory = SAXParserFactory.newInstance();
                 SAXParser saxParser = factory.newSAXParser();
                 saxParser.parse(zipInputStream, new DefaultHandler(){
                     boolean isWt = false;

                     @Override
                     public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                         if (qName.equals("w:t")) isWt = true;
                     }

                     @Override
                     public void characters(char[] ch, int start, int length) throws SAXException {
                         if (isWt){
                             out.println(new String(ch, start, length));
                             isWt = false;
                         }
                     }
                 });
                 System.out.println("Конвертация произведена!");
             } catch (FileNotFoundException e){
            System.out.println("Файл .docx не найден!");
        }
    }
}
