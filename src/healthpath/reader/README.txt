What is this directory?

I have two files.
1: The Rx Claims raw data which includes a PERSON_ID in each record.
2: The Master Death List which includes a PERSON_ID in each record.

These two datasets are related-but-different. The class CreatePeopleLookupFile ( and its associated helper objs ) 
is a one-time merge of those two datasets into one.  
  