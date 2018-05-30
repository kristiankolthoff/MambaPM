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

The [FrameNet project](https://framenet.icsi.berkeley.edu/fndrupal/) is building a lexical database consisting of thousands of manually annotated sentences trying to facilitate and improve the semantic analysis of words in sentences for machines. This framework is build on the notion of frame semantics. Basically, the meaning of words can be understood best if placed within a semantic frame describing for instance the events or participants. Such a semantic frame consists of multiple smaller fragments, called frame elements. A lexical unit evokes a frame and often provides the basis for detecting the surrounding components in the text which belong to a specific frame element.

![alt text](https://raw.githubusercontent.com/kristiankolthoff/MambaPM/master/src/main/resources/images/framenet.png "FrameNet example")

The figure above shows an example sentence annotated with semantic frames from the FrameNet project. In this example, the sentence is annotated with four distinct semantic frames. For example, the first frame is called **Sending** and was evoked by the word *'send'*. The **Sending** frame consists of the core frame elements Goal, Recipient, Sender and Theme. In addition, this frame consists of multiple other non-core frame elements such as the Container in which something is sent. However, in our example only the Theme and Goal frame elements are contained and assigned to the appropriate parts of the sentence. Thus, we discover that an *acceptance letter(Theme)* is sent(**Sending**-Frame) to an *student(Goal)*. Note that the student not only belongs to the frame element Goal of the **Sending** frame but also itself evokes the frame **Education Teaching** which refers to teaching or participants in teaching. For our process matching system, we use SEMAFOR \cite{das2010semafor,das2010probabilistic,das2014frame} since it is an implementation for automatic semantic role labeling based on FrameNet. It is trained with machine learning techniques and generates semantic frame annotations also for unseen sentences.

# Methodology

This section introduces \mamba, a process matching system combining semantic parsing using FrameNet \cite{baker1998berkeley} and distributional semantics using DISCO (extracting DIStributionally related words using CO-occurences) \cite{kolb2008disco}. We divide the process model alignment into two separate layers. The first layer is responsible for generating correspondences attached with a confidence value, by the means of both syntactic as well as semantic analysis of the activity labels. Hence, this layer is completely agnostic in terms of the underlying structure of the process model and only generates alignments based on the comparison of activity labels. Afterwards, the generated alignment is used as input to the second layer which is responsible for identifying and removing  previously created correspondences in such a way that the confidences are maximized subject to predefined rules. These rules introduce structural restrictions between activities. For example, is is very unlikely for similar activities to occur in completely different order. In the context of BPMN process models, a further structural restriction is, for instance, that two matching activities of a correspondence are likely to occur in the same swimlane. 

![alt text](https://raw.githubusercontent.com/kristiankolthoff/MambaPM/master/src/main/resources/images/alignment_generation.PNG "Two-Layer alignment generation")


The Figure illustrates the described two-layer process for generating alignments. The illustration contains abstractions of a BPMN process models, which neglects many important components of a process model, however, shows swimlanes and embedded activities as well as correspondences generated between two process models. Note that the main contribution of this paper is to present only the first layer in detail. However, the evaluation of the experiments also involves the application of the second layer.

## Syntactic Matching

## Semantic Matching
# References
