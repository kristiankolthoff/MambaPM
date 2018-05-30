# MambaPM

MambaPM is a process model matching tool for BPMN, PNML and EPK processes a process matching system. Our approach is based on a two-layer correspondence derivation process. In the first phase, we generate numerous correspondences attached with confidence scores. Therefore we firstly identify correspondences based on well-known NLP techniques focusing on the syntactical structure of the activity labels. Secondly, we generate correspondences employing automatic semantic role labeling to the activity labels based on the lexical database FrameNet in combination with distributional semantics by DISCO. After obtaining the correspondences of this approach,
we retrospectively delete correspondences in such a manner, that additionally structural information of the process model is exploited and the remaining correspondences maximize an optimization problem according to structural restrictions.

Generating highly accurate process model alignments is a difficult task, however, large amount of work has been put there to increase the matching algorithms. In this paper, we present a process model matching algorithm employing classical NLP as well as more sophisticated semantic role labeling techniques. Instead of solely relying on semantic role labeling, we incorporate a fuzzy component achieved by distributional semantics. Afterwards, the process model alignments based on both syntactic and semantic matching strategies are cleaned using Markov Logic in combination with structural restrictions on the correspondences.

# Preliminaries

## Distributional Semantics with DISCO

In general, distributional semantics is about harvesting large text corpora and
producing word vectors as a result. Multiple word vectors form a word space.
Based on these vector representations of words, we can compute semantic sim-
ilarities between words. It exploits the fact that similar words occur in similar
contexts. For our process matching system we particularly employ DISCO [6]
allowing us to compute the cosine similarity between two words based on a
precomputed word space.

## Semantic Role Labeling with FrameNet

The [FrameNet project](https://framenet.icsi.berkeley.edu/fndrupal/) is building a lexical database consisting of thousands of manually annotated sentences trying to facilitate and improve the semantic analysis of words in sentences for machines. This framework is build on the notion of frame semantics. Basically, the meaning of words can be understood best if placed within a semantic frame describing for instance the events or participants. Such a semantic frame consists of multiple smaller fragments, called frame elements. A lexical unit evokes a frame and often provides the basis for detecting the surrounding components in the text which belong to a specific frame element. Figure \ref{fig:framenet} shows an example sentence annotated with semantic frames from the FrameNet project. In this example, the sentence is annotated with four distinct semantic frames. For example, the first frame is called \textsc{Sending} and was evoked by the word 'send'. The \textsc{Sending} frame consists of the core frame elements Goal, Recipient, Sender and Theme. In addition, this frame consists of multiple other non-core frame elements such as the Container in which something is sent. However, in our example only the Theme and Goal frame elements are contained and assigned to the appropriate parts of the sentence. Thus, we discover that an acceptance letter(Theme) is sent(\textsc{Sending}-Frame) to an student(Goal). Note that the student not only belongs to the frame element Goal of the \textsc{Sending} frame but also itself evokes the frame \textsc{Education\_Teaching} which refers to teaching or participants in teaching. For our process matching system, we use SEMAFOR \cite{das2010semafor,das2010probabilistic,das2014frame} since it is an implementation for automatic semantic role labeling based on FrameNet. It is trained with machine learning techniques and generates semantic frame annotations also for unseen sentences.

# References
