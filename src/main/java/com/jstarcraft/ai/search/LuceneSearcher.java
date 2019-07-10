package com.jstarcraft.ai.search;

import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.NumericUtils;

/**
 * Lucene搜索器
 * 
 * @author Birdy
 *
 */
public interface LuceneSearcher {

    static final String ID = "id";

    static final String INSTANT = "instant";

    default String getId(Document document) {
        return document.get(ID);
    }

    default long getInstant(Document document) {
        BytesRef bytes = document.getBinaryValue(INSTANT);
        long instant = NumericUtils.sortableBytesToLong(bytes.bytes, bytes.offset);
        return instant;
    }

    default void setInstant(Document document, long instant) {
        byte[] bytes = new byte[Long.BYTES];
        NumericUtils.longToSortableBytes(instant, bytes, 0);
        StoredField field = new StoredField(INSTANT, bytes);
        document.add(field);
    }

    /**
     * 创建文档
     * 
     * @param documents
     * @throws Exception
     */
    void createDocuments(Document... documents) throws Exception;

    /**
     * 变更文档
     * 
     * @param documents
     * @throws Exception
     */
    void updateDocuments(Document... documents) throws Exception;

    /**
     * 删除文档
     * 
     * @param ids
     * @throws Exception
     */
    void deleteDocuments(String... ids) throws Exception;

    /**
     * 检索文档
     * 
     * @param query
     * @param sort
     * @param size
     * @return
     * @throws Exception
     */
    List<Document> retrieveDocuments(Query query, Sort sort, int size) throws Exception;

    /**
     * 统计文档
     * 
     * @param query
     * @return
     * @throws Exception
     */
    int countDocuments(Query query) throws Exception;

}