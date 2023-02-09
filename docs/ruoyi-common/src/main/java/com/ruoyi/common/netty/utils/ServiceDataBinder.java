/*     */ package com.ruoyi.common.netty.utils;
/*     */ 
/*     */ import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.validation.DataBinder;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ 
/*     */ public class ServiceDataBinder extends DataBinder
/*     */ {
/*     */   public static final String DEFAULT_FIELD_MARKER_PREFIX = "_";
/*     */   public static final String DEFAULT_FIELD_DEFAULT_PREFIX = "!";
/*  74 */   private String fieldMarkerPrefix = "_";
/*     */ 
/*  76 */   private String fieldDefaultPrefix = "!";
/*     */ 
/*  78 */   private boolean bindEmptyMultipartFiles = true;
/*     */ 
/*     */   public ServiceDataBinder(Object target)
/*     */   {
/*  88 */     super(target);
/*     */   }
/*     */ 
/*     */   public ServiceDataBinder(Object target, String objectName)
/*     */   {
/*  98 */     super(target, objectName);
/*     */   }
/*     */ 
/*     */   public void setFieldMarkerPrefix(String fieldMarkerPrefix)
/*     */   {
/* 125 */     this.fieldMarkerPrefix = fieldMarkerPrefix;
/*     */   }
/*     */ 
/*     */   public String getFieldMarkerPrefix()
/*     */   {
/* 132 */     return this.fieldMarkerPrefix;
/*     */   }
/*     */ 
/*     */   public void setFieldDefaultPrefix(String fieldDefaultPrefix)
/*     */   {
/* 151 */     this.fieldDefaultPrefix = fieldDefaultPrefix;
/*     */   }
/*     */ 
/*     */   public String getFieldDefaultPrefix()
/*     */   {
/* 158 */     return this.fieldDefaultPrefix;
/*     */   }
/*     */ 
/*     */   public void setBindEmptyMultipartFiles(boolean bindEmptyMultipartFiles)
/*     */   {
/* 170 */     this.bindEmptyMultipartFiles = bindEmptyMultipartFiles;
/*     */   }
/*     */ 
/*     */   public boolean isBindEmptyMultipartFiles()
/*     */   {
/* 177 */     return this.bindEmptyMultipartFiles;
/*     */   }
/*     */ 
/*     */   protected void doBind(MutablePropertyValues mpvs)
/*     */   {
/* 189 */     checkFieldDefaults(mpvs);
/* 190 */     checkFieldMarkers(mpvs);
/* 191 */     super.doBind(mpvs);
/*     */   }
/*     */ 
/*     */   protected void checkFieldDefaults(MutablePropertyValues mpvs)
/*     */   {
/* 203 */     if (getFieldDefaultPrefix() != null) {
/* 204 */       String fieldDefaultPrefix = getFieldDefaultPrefix();
/* 205 */       PropertyValue[] pvArray = mpvs.getPropertyValues();
/* 206 */       for (PropertyValue pv : pvArray)
/* 207 */         if (pv.getName().startsWith(fieldDefaultPrefix)) {
/* 208 */           String field = pv.getName().substring(fieldDefaultPrefix.length());
/* 209 */           if ((getPropertyAccessor().isWritableProperty(field)) && (!mpvs.contains(field))) {
/* 210 */             mpvs.add(field, pv.getValue());
/*     */           }
/* 212 */           mpvs.removePropertyValue(pv);
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void checkFieldMarkers(MutablePropertyValues mpvs)
/*     */   {
/* 230 */     if (getFieldMarkerPrefix() != null) {
/* 231 */       String fieldMarkerPrefix = getFieldMarkerPrefix();
/* 232 */       PropertyValue[] pvArray = mpvs.getPropertyValues();
/* 233 */       for (PropertyValue pv : pvArray)
/* 234 */         if (pv.getName().startsWith(fieldMarkerPrefix)) {
/* 235 */           String field = pv.getName().substring(fieldMarkerPrefix.length());
/* 236 */           if ((getPropertyAccessor().isWritableProperty(field)) && (!mpvs.contains(field))) {
/* 237 */             Class fieldType = getPropertyAccessor().getPropertyType(field);
/* 238 */             mpvs.add(field, getEmptyValue(field, fieldType));
/*     */           }
/* 240 */           mpvs.removePropertyValue(pv);
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Object getEmptyValue(String field, Class<?> fieldType)
/*     */   {
/* 256 */     if (((fieldType != null) && (Boolean.TYPE.equals(fieldType))) || (Boolean.class.equals(fieldType)))
/*     */     {
/* 258 */       return Boolean.FALSE;
/*     */     }
/* 260 */     if ((fieldType != null) && (fieldType.isArray()))
/*     */     {
/* 262 */       return Array.newInstance(fieldType.getComponentType(), 0);
/*     */     }
/*     */ 
/* 266 */     return null;
/*     */   }
/*     */ 
/*     */   protected void bindMultipart(Map<String, List<MultipartFile>> multipartFiles, MutablePropertyValues mpvs)
/*     */   {
/* 281 */     for (Entry entry : multipartFiles.entrySet()) {
/* 282 */       String key = (String)entry.getKey();
/* 283 */       List values = (List)entry.getValue();
/* 284 */       if (values.size() == 1) {
/* 285 */         MultipartFile value = (MultipartFile)values.get(0);
/* 286 */         if ((isBindEmptyMultipartFiles()) || (!value.isEmpty()))
/* 287 */           mpvs.add(key, value);
/*     */       }
/*     */       else
/*     */       {
/* 291 */         mpvs.add(key, values);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-esb\1.0.4-SNAPSHOT\gsoft-framework-esb-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.esb.bind.ServiceDataBinder
 * JD-Core Version:    0.6.0
 */