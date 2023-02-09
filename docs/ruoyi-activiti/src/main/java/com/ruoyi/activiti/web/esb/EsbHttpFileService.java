package com.ruoyi.activiti.web.esb;

import com.ruoyi.activiti.web.esb.entity.FileStore;
import org.apache.http.client.ClientProtocolException;

import java.io.File;
import java.io.IOException;

public abstract interface EsbHttpFileService
{
  public abstract String uploadFile(String paramString1, File paramFile, String paramString2, String paramString3, String paramString4)
    throws IOException;

  public abstract File downloadFile(String paramString1, String paramString2)
    throws ClientProtocolException, IOException;

  public abstract void delFile(String paramString);

  public abstract FileStore getFileStoreByPath(String paramString);

  public abstract void updateFileStore(FileStore paramFileStore);
}
