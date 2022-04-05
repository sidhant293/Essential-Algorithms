# Twitter Snowflake

The properties of twitter snowflake are-
-	IDs are unique
-	IDs have numeric value only
-	IDs fit into 64 bits
-	IDs are ordered by date
-	More than 1,00,000 IDs are generated per second

## ID Architecture

| 1 bit | 41 bits | 5 bits | 5 bits | 12 bits |
| :---- | :------ | :----- | :----- | :------ |
| Sign bit | timestamp | datacenter ID | machine ID | sequence number |

-	Sign bit - (1-bit) This is always 0. It is reserved for future uses. It differentiates between signed and unsigned bits
-	Timestamp - (41-bit) It has a precision till millisecond. A default epoc time (128834874657) is added to timestamp to convert it to UTC
-	Datacenter ID – (5-bit) This gives us 2^5 datacenters
-	Machine ID – (5-bit) This gives us 2^5 machines per datacenters
-	Sequence Number – (12-bit) For every ID that is generated on same millisecond, it is incremented by 1. Next millisecond again it is reset to 0


