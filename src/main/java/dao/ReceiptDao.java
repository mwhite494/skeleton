package dao;

import api.ReceiptResponse;
import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TagsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.RECEIPTS;
import static generated.Tables.TAGS;

public class ReceiptDao {
    DSLContext dsl;

    public ReceiptDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }

    public int insert(String merchantName, BigDecimal amount) {
        ReceiptsRecord receiptsRecord = dsl
                .insertInto(RECEIPTS, RECEIPTS.MERCHANT, RECEIPTS.AMOUNT)
                .values(merchantName, amount)
                .returning(RECEIPTS.ID)
                .fetchOne();

        checkState(receiptsRecord != null && receiptsRecord.getId() != null, "Insert failed");

        return receiptsRecord.getId();
    }

    public List<ReceiptsRecord> getAllReceipts() {
        return dsl.selectFrom(RECEIPTS).fetch();
    }

    public void tagReceipt(String tagName, Integer receiptId) {
        TagsRecord checkIfExists = dsl.selectFrom(TAGS)
                .where(TAGS.TAG.eq(tagName))
                .and(TAGS.ID.eq(receiptId))
                .fetchOne();

        if (checkIfExists == null) {
            dsl.insertInto(TAGS, TAGS.ID, TAGS.TAG)
                    .values(receiptId, tagName)
                    .execute();
        }
        else {
            dsl.delete(TAGS)
                    .where(TAGS.TAG.eq(tagName))
                    .and(TAGS.ID.eq(receiptId))
                    .execute();
        }

    }

    public List<ReceiptsRecord> getTags(String tagName) {
        List<ReceiptsRecord> taggedReceipts = dsl.select()
                .from(RECEIPTS)
                .join(TAGS).on(TAGS.ID.eq(RECEIPTS.ID))
                .where(TAGS.TAG.eq(tagName))
                .fetchInto(RECEIPTS);

        return taggedReceipts;
    }

    public List<TagsRecord> getAllTags() { return dsl.selectFrom(TAGS).fetch(); }

}
