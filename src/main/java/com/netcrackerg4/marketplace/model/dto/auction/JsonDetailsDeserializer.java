package com.netcrackerg4.marketplace.model.dto.auction;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.netcrackerg4.marketplace.service.interfaces.IAuctionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JsonDetailsDeserializer extends JsonDeserializer<JsonDetails> {
    private final IAuctionService auctionService;

    @Override
    public JsonDetails deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec codec = jsonParser.getCodec();
        JsonNode node =  codec.readTree(jsonParser);
        int typeId = ((AuctionDto) jsonParser.getParsingContext().getCurrentValue()).getTypeId();
        String name = this.auctionService.getTypeNameById(typeId);
        if(name.equals("ASCENDING")) return codec.treeToValue(node, AscendingJsonDetails.class);
        else return codec.treeToValue(node, DescendingJsonDetails.class);
    }
}
