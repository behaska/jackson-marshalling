# jackson-marshalling

A sample project that have 2 main classes. One in *Java* and one in *Kotlin*.

A model class of type Catalog with subclasses is generated from an Open API spec.

In the main, we unmarshall the content of a String (json) into the generated model with Jackson.

It shows the need of having a KotlinModule in the Jackson mapper to be able to use the generated model classes (Kotlin data classes).  