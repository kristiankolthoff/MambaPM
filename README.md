# MambaPM

MambaPM is a process model matching tool for BPMN, PNML and EPK processes a process matching system. Our approach is based on a two-layer correspondence derivation process. In the first phase, we generate numerous correspondences attached with confidence scores. Therefore we firstly identify correspondences based on well-known NLP techniques focusing on the syntactical structure of the activity labels. Secondly, we generate correspondences employing automatic semantic role labeling to the activity labels based on the lexical database FrameNet in combination with distributional semantics by DISCO. After obtaining the correspondences of this approach,
we retrospectively delete correspondences in such a manner, that additionally structural information of the process model is exploited and the remaining correspondences maximize an optimization problem according to structural restrictions.

Generating highly accurate process model alignments is a difficult task, however, large amount of work has been put there to increase the matching algorithms. In this paper, we present a process model matching algorithm employing classical NLP as well as more sophisticated semantic role labeling techniques. Instead of solely relying on semantic role labeling, we incorporate a fuzzy component achieved by distributional semantics. Afterwards, the process model alignments based on both syntactic and semantic matching strategies are cleaned using Markov Logic in combination with structural restrictions on the correspondences.

# Preliminaries

## a. Distributional Semantics with DISCO

In general, distributional semantics is about harvesting large text corpora and
producing word vectors as a result. Multiple word vectors form a word space.
Based on these vector representations of words, we can compute semantic sim-
ilarities between words. It exploits the fact that similar words occur in similar
contexts. For our process matching system we particularly employ DISCO [6]
allowing us to compute the cosine similarity between two words based on a
precomputed word space.

## b. Semantic Role Labeling with FrameNet

The [FrameNet project](https://framenet.icsi.berkeley.edu/fndrupal/) is building a lexical database consisting of thousands of manually annotated sentences trying to facilitate and improve the semantic analysis of words in sentences for machines. This framework is build on the notion of frame semantics. Basically, the meaning of words can be understood best if placed within a semantic frame describing for instance the events or participants. Such a semantic frame consists of multiple smaller fragments, called frame elements. A lexical unit evokes a frame and often provides the basis for detecting the surrounding components in the text which belong to a specific frame element.

![alt text](https://raw.githubusercontent.com/kristiankolthoff/MambaPM/master/src/main/resources/images/framenet.png "FrameNet example")

The figure above shows an example sentence annotated with semantic frames from the FrameNet project. In this example, the sentence is annotated with four distinct semantic frames. For example, the first frame is called **Sending** and was evoked by the word *'send'*. The **Sending** frame consists of the core frame elements Goal, Recipient, Sender and Theme. In addition, this frame consists of multiple other non-core frame elements such as the Container in which something is sent. However, in our example only the Theme and Goal frame elements are contained and assigned to the appropriate parts of the sentence. Thus, we discover that an *acceptance letter(Theme)* is sent(**Sending**-Frame) to an *student(Goal)*. Note that the student not only belongs to the frame element Goal of the **Sending** frame but also itself evokes the frame **Education Teaching** which refers to teaching or participants in teaching. For our process matching system, we use SEMAFOR \cite{das2010semafor,das2010probabilistic,das2014frame} since it is an implementation for automatic semantic role labeling based on FrameNet. It is trained with machine learning techniques and generates semantic frame annotations also for unseen sentences.

# Methodology

This section introduces \mamba, a process matching system combining semantic parsing using FrameNet \cite{baker1998berkeley} and distributional semantics using DISCO (extracting DIStributionally related words using CO-occurences) \cite{kolb2008disco}. We divide the process model alignment into two separate layers. The first layer is responsible for generating correspondences attached with a confidence value, by the means of both syntactic as well as semantic analysis of the activity labels. Hence, this layer is completely agnostic in terms of the underlying structure of the process model and only generates alignments based on the comparison of activity labels. Afterwards, the generated alignment is used as input to the second layer which is responsible for identifying and removing  previously created correspondences in such a way that the confidences are maximized subject to predefined rules. These rules introduce structural restrictions between activities. For example, is is very unlikely for similar activities to occur in completely different order. In the context of BPMN process models, a further structural restriction is, for instance, that two matching activities of a correspondence are likely to occur in the same swimlane. 

![alt text](https://raw.githubusercontent.com/kristiankolthoff/MambaPM/master/src/main/resources/images/alignment_generation.PNG "Two-Layer alignment generation")


The Figure illustrates the described two-layer process for generating alignments. The illustration contains abstractions of a BPMN process models, which neglects many important components of a process model, however, shows swimlanes and embedded activities as well as correspondences generated between two process models. Note that the main contribution of this paper is to present only the first layer in detail. However, the evaluation of the experiments also involves the application of the second layer.

## a. Syntactic Matching

The first and straightforward step for generating the process model alignment is achieved by identifying correspondences by syntactically matching activity labels. That is, we use well-known NLP techniques to preprocess the activity labels, and then generate the final correspondences by evaluating the string similarity and equality. This syntactic activity matching phase is capable of producing correspondences which are easy to identify. The basic NLP steps we apply to the activity labels are the following. First, we simply lower case all letters. Afterwards, we apply stemming of each word in the label to obtain the stems of the word. To produce the final correspondence, we check for string equality after applying all normalization techniques. Note that the appropriate confidence score of an identified correspondence is set to 1 manually, since we generate only correct correspondences with the described syntactic matching approach.

## b. Semantic Matching

After applying the simple syntactic activity matching, the semantic activity matching component receives all remaining activity pairs. Hence, the characteristics of the activity pair the system tries to match are beyond the scope of simple syntactic matching. Therefore, we apply, as a second step, a more sophisticated semantic matching of the activity labels. To achieve this, we combine semantic annotation by FrameNet with distributional semantics. Incorporating distributional semantics in the frame generation process, at the same time incorporates a fuzzy component and facilitates to recognize correspondences which are semantically similar. 

![alt text](https://raw.githubusercontent.com/kristiankolthoff/MambaPM/master/src/main/resources/images/framenet_activity.PNG "Semantic matching approach using FrameNet")

The Figure illustrates the general notion of semantic activity matching using FrameNet annotations. In the presented example, the semantic matching component tests whether activity **A1** with the label **l1** matches activity **A2** with corresponding label **l2**, as we would typically observe in a university application process model. First of all, we apply POS-Tagging of the activity label to identify the verb. Afterwards, we generate *k*-many additional labels which are similar to the basic activity label, however, with its verb replaced by a similar word computed by the distributional semantics framework DISCO. We denote such a label by similar label **s** for the rest of the description. Note that the primarily label is a similar label trivially. The verb replacements are ordered descending based on the cosine similarity to the primarily label. DISCO also computes similar words which are no verbs. To identify and retain only similar verbs, we use the WordNet dictionary \cite{miller1995wordnet,leacock1998combining,pedersen2004wordnet}. Each of the *k* similar labels are semantically annotated with semantic frames of FrameNet. Each similar label can trigger *n*-many frames. Overall we obtain *k x n*-many frames for each activity.

After deriving all frames following the procedure described previously for each of the two activities, we compare the resulting frame colletions. Here we use two distinct comparison strategies for generating the final correspondence between activity **A1** and **A2**. 

1. Single frame identical
	
If only one frame of the two frame collections is identical, we generate a correspondence **C**. The confidence score for **C** is computed as **conf(C) = k / (k+1)**.

2. Majority vote frame identical
	
If the majority vote frame of each of the two frame collections is identical, we generate a correspondence **C**. To compute the majority vote frame, we iterate over the *k* similar labels and increase the frame count for frame **f**, denoted as *count(f)*, if a frame was triggered at the current similar label. Hence, the maximum count for a frame is *k*, since a frame can only be evoked once per label. The majority vote frame **f** has the largest *count(f)* value among all produced frames. Note that the majority vote frame is actually a frame set, since different frames can have the same maximum count. If we obtain two majority vote frame sets, only one frame of them is required to be equal to generate a correspondence **C**. The confidence score for the generated correspondence **C** is computed as **conf(C) = min(count(f1),count(f2)) / (k+1)** with **f1** being the majority vote frame of activity **A1** and **f2** being the majority vote frame of **A2**.

The figure above shows an explicit example taken from a set of university application process models. The two activity labels involved are semantically very identical, however, the simple syntactic matching component is not able to detect the correspondence. Let frame number parameter *k = 2*. Since 'Sending' is identified as the verb in **l1**, we generate the two similar labels. As the first similar label, we use the primarily label itself. To derive a second similar label, we replace 'Sending' by 'Dispatch', which has a high cosine similarity according to DISCO. For these two similar labels, three equal frames are evoked which are **Amassing**, **Text** and **Education Teaching**. However, the **Sending** frame is only triggered for the first label. Hence, we obtain *count$(Text) = 2* and *count(Sending) = 1* for activity **A1**. Therefore, the majority vote frame set consists of the three frames having a count of 2. The majority vote frame set for activity **A2** is identical to the previously described one. Finally, we generate the correspondence **C** between **A1** and **A2** with **conf(C) = 2/3**.

# References
