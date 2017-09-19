package api;

import com.fasterxml.jackson.annotation.JsonProperty;
import generated.tables.records.TagsRecord;

public class TagResponse {
    @JsonProperty
    Integer id;

    @JsonProperty
    String tagName;

    public TagResponse(TagsRecord dbRecord) {
        this.id = dbRecord.getId();
        this.tagName = dbRecord.getTag();
    }
}
