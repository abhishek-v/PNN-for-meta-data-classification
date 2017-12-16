# PNN-for-meta-data-classification

Three formats of meta-data files have been considered for this classification : FGDC, NSDI and INSPIRE compliant meta-data files.

For example , FGDC.txt contains the attributes which make up the FGDM meta-data.
Similarly, ISRO and INSPIRE have a bunch of attributes in their files.

These attributes are represented in the md.csv file in the following manner: columns 1 to 85 represent FGDC, 86 to 124 represent INSPIRE file and the remaining for NSDI.

The code for the PNN is in pnn.java

There are 150 tuples. This can be split into 100 for training and 50 for testing.
