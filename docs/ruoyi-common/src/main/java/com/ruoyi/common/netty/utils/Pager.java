/*     */ package com.ruoyi.common.netty.utils;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class Pager
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 8676370649225208176L;
/*  17 */   public static int DEFALUT_PAGESIZE = 20;
/*     */ 
/*  19 */   private static int DEFALUT_PAGEINDEX = 1;
/*     */   public static final int QUERY_TYPE_ALL = 0;
/*     */   public static final int QUERY_TYPE_LIST = 1;
/*     */   public static final int QUERY_TYPE_COUNT = 2;
/*     */   public static final String EXPORT_TYPE_XLS = "xls";
/*     */   public static final String EXPORT_TYPE_PDF = "pdf";
/*     */   public static final String EXPORT_TYPE_PRINT = "print";
/*     */   private int pageSize;
/*     */   private int pageIndex;
/*     */   private int pageType;
/*     */   private int startIndex;
/*     */   private int counts;
/*     */   private String[] pagerProperties;
/*     */   private String export;
/*     */   private String[] exportHeaders;
/*     */   private String[] exportProperties;
/*     */   private String[] exportConverts;
/*     */ 
/*     */   public Pager(String pageSize, String pageIndex, String pageType)
/*     */   {
/*     */     try
/*     */     {
/*  55 */       this.pageSize = new Integer(pageSize).intValue();
/*     */     } catch (NumberFormatException e) {
/*  57 */       this.pageSize = DEFALUT_PAGESIZE;
/*     */     }
/*     */     try
/*     */     {
/*  61 */       this.pageIndex = new Integer(pageIndex).intValue();
/*     */     } catch (NumberFormatException e) {
/*  63 */       this.pageIndex = DEFALUT_PAGEINDEX;
/*     */     }
/*     */     try
/*     */     {
/*  67 */       this.pageType = new Integer(pageType).intValue();
/*     */     } catch (NumberFormatException e) {
/*  69 */       this.pageType = 0;
/*     */     }
/*  71 */     initPager();
/*     */   }
/*     */ 
/*     */   public Pager(int pageSize, int pageIndex, int pageType) {
/*  75 */     this.pageIndex = pageIndex;
/*  76 */     this.pageSize = pageSize;
/*  77 */     this.pageType = pageType;
/*  78 */     initPager();
/*     */   }
/*     */ 
/*     */   private void initPager()
/*     */   {
/*  86 */     this.pageIndex = (this.pageIndex < 1 ? 1 : this.pageIndex);
/*  87 */     this.startIndex = ((this.pageIndex - 1) * this.pageSize);
/*     */   }
/*     */ 
/*     */   public int getCounts() {
/*  91 */     return this.counts;
/*     */   }
/*     */ 
/*     */   public void setCounts(int counts) {
/*  95 */     this.counts = counts;
/*  96 */     int maxPage = getMaxPage();
/*  97 */     if ((maxPage > 0) && (this.pageIndex > maxPage)) {
/*  98 */       this.pageIndex = maxPage;
/*     */     }
/* 100 */     initPager();
/*     */   }
/*     */ 
/*     */   public int getPageIndex() {
/* 104 */     return this.pageIndex;
/*     */   }
/*     */ 
/*     */   public int getMaxPage()
/*     */   {
/* 109 */     double maxPage = Math.ceil(new Double(this.counts).doubleValue() / this.pageSize);
/* 110 */     return Double.valueOf(maxPage).intValue();
/*     */   }
/*     */ 
/*     */   public void setPageIndex(int pageIndex) {
/* 114 */     this.pageIndex = pageIndex;
/*     */   }
/*     */ 
/*     */   public int getPageSize() {
/* 118 */     return this.pageSize;
/*     */   }
/*     */ 
/*     */   public void setPageSize(int pageSize) {
/* 122 */     this.pageSize = pageSize;
/*     */   }
/*     */ 
/*     */   public int getStartIndex() {
/* 126 */     return this.startIndex;
/*     */   }
/*     */ 
/*     */   public void setStartIndex(int startIndex) {
/* 130 */     this.startIndex = startIndex;
/*     */   }
/*     */ 
/*     */   public int getPageType()
/*     */   {
/* 138 */     return this.pageType;
/*     */   }
/*     */ 
/*     */   public void setPageType(int pageType)
/*     */   {
/* 146 */     this.pageType = pageType;
/*     */   }
/*     */ 
/*     */   public String getExport()
/*     */   {
/* 154 */     return this.export;
/*     */   }
/*     */ 
/*     */   public void setExport(String export)
/*     */   {
/* 161 */     this.export = export;
/*     */   }
/*     */ 
/*     */   public String[] getExportHeaders()
/*     */   {
/* 169 */     return this.exportHeaders;
/*     */   }
/*     */ 
/*     */   public void setExportHeaders(String[] exportHeaders)
/*     */   {
/* 177 */     this.exportHeaders = exportHeaders;
/*     */   }
/*     */ 
/*     */   public String[] getExportProperties()
/*     */   {
/* 185 */     return this.exportProperties;
/*     */   }
/*     */ 
/*     */   public void setExportProperties(String[] exportProperties)
/*     */   {
/* 193 */     this.exportProperties = exportProperties;
/*     */   }
/*     */ 
/*     */   public String[] getPagerProperties()
/*     */   {
/* 200 */     return this.pagerProperties;
/*     */   }
/*     */ 
/*     */   public void setPagerProperties(String[] pagerProperties)
/*     */   {
/* 207 */     this.pagerProperties = pagerProperties;
/*     */   }
/*     */ 
/*     */   public String[] getExportConverts() {
/* 211 */     return this.exportConverts;
/*     */   }
/*     */ 
/*     */   public void setExportConverts(String[] exportConverts) {
/* 215 */     this.exportConverts = exportConverts;
/*     */   }
/*     */ }

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-core\1.0.4-SNAPSHOT\gsoft-framework-core-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.core.orm.Pager
 * JD-Core Version:    0.6.0
 */