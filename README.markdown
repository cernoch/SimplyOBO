SimplyOBO
=========

SimplyOBO is a light-weight Java library for parsing and inferencing inside the
OBO format. The OBO ontology format is used in the [Gene Ontology][1] project.

Introduction
------------

SimplyOBO uses three levels of abstraction:

### OBO file format syntax

The [OBO file][2] recognizes two primitives: *tag-value pairs* and *stanzas*.
The former stores of information and the latter is used to group them into
larger pieces.

See the LineByLineParser class for more information on parsing tag-value pairs
and stanza name primitives.

Since there is no semantics defined at this level, OBO file format can be used
to encode any piece of data (non-ontologies).

### OBO ontology primitives

TO BE DONE

### Reasoning


[1]: http://www.geneontology.org/	"Gene Ontology website"
[2]: http://www.geneontology.org/GO.format.obo-1_2.shtml#S.1.1	"OBO Document Structure"

