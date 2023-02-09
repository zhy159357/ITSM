//package com.ruoyi.es.utils;
//
//import com.ruoyi.common.utils.StringUtils;
//import org.apache.poi.hwpf.HWPFDocument;
//import org.apache.poi.hwpf.model.StyleDescription;
//import org.apache.poi.hwpf.model.StyleSheet;
//import org.apache.poi.hwpf.usermodel.Paragraph;
//import org.apache.poi.hwpf.usermodel.Range;
//import org.apache.poi.xwpf.usermodel.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
///**
// * 类别Controller
// * @author dayong_sun
// * @date 2021-04-12
// */
//public class WordUtils {
//
//    protected static final Logger logger = LoggerFactory.getLogger(WordUtils.class);
//
//    public static void main(String[] args) throws IOException {
//        String msg = "";
//        String path = "D:\\我的文档\\WeChat Files\\wxid_5mfwrxeej77m22\\FileStorage\\File\\2021-04\\sdy_知识库2021-03-25 10.30(1)(1)(1)(1)(2)(1).doc";
////        path = "E:\\刚哥\\磊哥\\vpn资料\\附件2：中国邮政储蓄银行远程开发协作办公平台保密承诺书_郭刚.doc";
//        if(path.endsWith(".doc")) {
//            msg = getWordTitles2003(path);
//        }else if (path.endsWith(".docx")) {
//            msg = getWordTitles2007(path);
//        }
//        logger.info("==================="+msg);
//    }
//
//    public static String getWordTitles(String path) {
//        String msg = "";
//        try{
//            if(path.endsWith(".doc")) {
//                msg = getWordTitles2003(path);
//            }else if (path.endsWith(".docx")) {
//                msg = getWordTitles2007(path);
//            }
//        }catch(Exception e){
//            msg = "";
//        }
//        return msg;
//    }
//    /** * 遍历段落内容
//     * docxReadPath 文档地址
//     * uploadPic 图片上传地址
//     * picFile 图片保存后地址
//     * @param path
//     * @return XWPFDocument
//     * @throws IOException
//     */
//    public static String getWordTitles2007(String path){
//        String msg = "";
//        String[] sp = path.split("\\.");
//        try {
//            if ((sp.length > 0)&&sp[sp.length - 1].equalsIgnoreCase("docx")) {
//                FileInputStream fis = new FileInputStream(path);
//                XWPFDocument document = new XWPFDocument(fis);
//                Iterator<XWPFParagraph> itPara = document.getParagraphsIterator();
//                //读取word中所有内容
//                while (itPara.hasNext()) {
//                    XWPFParagraph paragraph = (XWPFParagraph) itPara.next();
//                    String style = paragraph.getStyle();
//                    String text = paragraph.getParagraphText().trim();
//                    logger.info("--------style-----------" + style);
//                    //run表示相同区域属性相同的字符，结果以‘，’分隔；
//                    List<XWPFRun> runs = paragraph.getRuns();// paragraph.getRuns();
//                    for (int i = 0; i < runs.size(); i++) {
//                        if ("8".equals(style) || "7".equals(style)) {
//                            String oneparaString = runs.get(i).getText(runs.get(i).getTextPosition());
//                            if (StringUtils.isNotEmpty(oneparaString)&&!msg.contains(text)) {
//                                msg += oneparaString + ",";
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            msg = "";
//        }
//        return msg;
//    }
//
//    public static String getWordTitles2003(String path) throws IOException {
//        String msg = "";
//        InputStream is = new FileInputStream(path);
//        HWPFDocument doc = new HWPFDocument(is);
//        Range r = doc.getRange();
//        List<String> list = new ArrayList<String>();
//        for (int i = 0; i < r.numParagraphs(); i++) {
//            Paragraph p = r.getParagraph(i);
//            int numStyles = doc.getStyleSheet().numStyles();
//            int styleIndex = p.getStyleIndex();
//            if (numStyles > styleIndex) {
//                StyleSheet style_sheet = doc.getStyleSheet();
//                StyleDescription style = style_sheet.getStyleDescription(styleIndex);
//                String styleName = style.getName();
//                if (styleName != null && styleName.contains("标题")) {
//                    String text = p.text();
//                    if(!msg.contains(text)) {
//                        msg += text + ",";
//                    }
//                }
//            }
//        }
//        return msg;
//    }
//}
