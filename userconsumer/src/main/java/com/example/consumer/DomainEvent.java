package com.example.consumer;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Date;

@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "type")
@JsonSubTypes({
		@JsonSubTypes.Type(name = "card.assigned", value = UserCreated.class),
		@JsonSubTypes.Type(name = "card.created", value = UserActivated.class),
		@JsonSubTypes.Type(name = "card.repaid", value = UserNameChanged.class),
		@JsonSubTypes.Type(name = "card.withdrawn", value = UserDeactivated.class)
})
public interface DomainEvent {
	Date getOccuredAt();
}
