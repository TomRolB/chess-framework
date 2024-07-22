# Chess Framework

This framework is part of an extensive course from Universidad Austral on the design of software systems. It includes the following features:

<figure>
  <img src="/diagrams/Capablanca's%20Chess.png" alt="Image description">
  <figcaption>Capablanca's Chess run over the framework</figcaption>
</figure>

* **Working configurations** for **Chess** and **Checkers**, with tests included.
* A **rule engine** to create pieces (e.g. a pawn for Chess, or a Man for Checkers), general movement rules, winning conditions, etc. for a board game.
* A **user interface** to play the defined board game.
* A **server** which supports a client for each player and extra clients for spectators.

The core idea of this framework is to have an _onion architecture_, where the engine is completely independent of any app using it.
This structure, based on the **SOLID** principles and different **design patterns**, greatly eased the development of the UI, the client-server component and the tests for Chess and Checkers.

Additionally, games can be easily defined and configured using the developed rule components. For instance, this repository includes not only classic Chess and Checkers examples but also various developed variants.
