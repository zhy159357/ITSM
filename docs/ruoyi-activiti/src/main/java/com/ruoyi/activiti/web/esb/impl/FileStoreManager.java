package com.ruoyi.activiti.web.esb.impl;

import com.ruoyi.activiti.web.esb.entity.FileStore;
import com.ruoyi.common.core.Condition;
import com.ruoyi.common.core.Order;
import com.ruoyi.common.netty.utils.Pager;
import com.ruoyi.common.netty.utils.PagerRecords;

import javax.swing.tree.TreeNode;
import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

public interface FileStoreManager {
    public FileStore storeFile(String paramString1, InputStream paramInputStream, String paramString2, String paramString3, String paramString4);

    public FileStore storeFile(String paramString1, InputStream paramInputStream, String paramString2, String paramString3);

    public File getFile(String paramString);

    public File getAbsoluteFile(String paramString);

    public void delFile(String paramString);

    public void updateFileStore(FileStore paramFileStore);

    public FileStore getFileStoreByPath(String paramString);

    public void markedUsed(String paramString);

    public PagerRecords getPagerFileStoresByUser(Pager paramPager, Collection<Condition> paramCollection, Collection<Order> paramCollection1);

    public List<TreeNode> getGroupTreesByUser(String paramString);
}
