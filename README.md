# MambaPM
MambaPM is a process model matching tool for BPMN, PNML and EPK processes 
a process matching system. Our approach is based on a two-layer correspondence derivation process.
In the first phase, we generate numerous correspondences attached with
confidence scores. Therefore we firstly identify correspondences based
on well-known NLP techniques focusing on the syntactical structure
of the activity labels. Secondly, we generate correspondences employing automatic semantic role labeling to the activity labels based on the
lexical database FrameNet in combination with distributional semantics by DISCO. After obtaining the correspondences of this approach,
we retrospectively delete correspondences in such a manner, that additionally structural information of the process model is exploited and the
remaining correspondences maximize an optimization problem according
to structural restrictions.
