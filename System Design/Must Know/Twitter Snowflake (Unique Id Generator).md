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

## Timestamp

Timestamp contains 41 bits. These bits are first converted into decimal. Then default epoc time is added. After addition it gives milliseconds. This millisecond is then converted into UTC time.
The highest value timestamp can have been 2^41-1 = 2199023255551 ms

Converting ms to years= 2199023255551ms / 1000 / 365 days/24hrs/3600 sec = 69 years. This approach will run of 69 years.

## Datacenter ID and Machine ID

Datacenter IDs and machine IDs are chosen at the startup. These values do not change as that will lead to ID conflicts. Once the system is up and running these values are fixed for specific datacenters and machines. 

## Sequence Number

Sequence Number is of 12 bits. 2^12 = 4096 combinations.

Value of sequence number is 0 initially unless there are multiple IDs getting generated in 1 millisecond. Value will only increment if there are multiple IDs generated in 1 millisecond. 

So in 1 millisecond it can generate 4096 values. That means in 1 sec it can generate 40,96,000 values.
