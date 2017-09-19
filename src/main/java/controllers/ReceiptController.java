package controllers;

import api.CreateReceiptRequest;
import api.ReceiptResponse;
import api.TagResponse;
import dao.ReceiptDao;
import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TagsRecord;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ReceiptController {
    final ReceiptDao receipts;

    public ReceiptController(ReceiptDao receipts) {
        this.receipts = receipts;
    }

    @POST
    @Path("/receipts")
    public int createReceipt(@Valid @NotNull CreateReceiptRequest receipt) {
        return receipts.insert(receipt.merchant, receipt.amount);
    }

    @GET
    @Path("/receipts")
    public List<ReceiptResponse> getReceipts() {
        List<ReceiptsRecord> receiptRecords = receipts.getAllReceipts();
        return receiptRecords.stream().map(ReceiptResponse::new).collect(toList());
    }

    @GET
    @Path("/tags")
    public List<TagResponse> getTags() {
        List<TagsRecord> tagsRecords = receipts.getAllTags();
        return tagsRecords.stream().map(TagResponse::new).collect(toList());
    }

    @PUT
    @Path("/tags/{tag}")
    public void toggleTag(@PathParam("tag") String tagName, @Valid @NotNull Integer receiptId) {
        receipts.tagReceipt(tagName, receiptId);
    }

    @GET
    @Path("/tags/{tag}")
    public List<ReceiptResponse> getTaggedReceipts(@PathParam("tag") String tagName) {
        List<ReceiptsRecord> taggedReceipts = receipts.getTags(tagName);
        return taggedReceipts.stream().map(ReceiptResponse::new).collect(toList());
    }

    @GET
    @Path("/netid")
    @Produces(MediaType.TEXT_PLAIN)
    public String getNetId() {
        String netid = "mtw79";
        return netid;
    }

}
