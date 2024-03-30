# SaprBar 
### _Short description_
This is the developed system for automating strength calculations of rod systems, experiencing tension and compression, 
in the field of "computer graphics," accepts input data in the preprocessor (either through a txt file or directly manually within the program) 
and generates graphs based on calculations of the stress-strain state components of the structure (axial forces N(x), normal stresses σ(x), and displacements U(x).
This system is designed to facilitate the analysis of rod systems under various loads by efficiently processing input data and providing visual 
representations of the structural behavior through graphs depicting longitudinal forces, normal stresses, and displacements along the structure. 
The integration of this automation tool within the field of computer graphics allows for a more intuitive and comprehensive understanding of the 
mechanical response of rod systems to different loading conditions.

### _Detailed description_
Each rod [i] is characterized by: 
* [Li] - length
* [Ai] - cross-sectional area

The material of the rods is characterized by the elastic modulus [Ei] and the permissible
voltage [σi].

The rods can be affected by:
* [Horizontal force]
* [Left force]
* [Right force]

The presence of left and right support is determined by the [Support left] and [Support right] variables
