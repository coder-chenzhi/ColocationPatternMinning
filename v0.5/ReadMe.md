I've found the star instance prevalence filter is useless in most case, so I omit this
step in code. And there are another reason for doing this, the star instance is far
more frequent and space-consuming than clique instance, so I tend to generate clique
instance directlly when getting the star instance. That is, I don't need to store the
star instance, which can save much space.

However, the collection in Java is far from memory-friendly. No matter how I optimize my code,
the data amount I can handle is limited. So I intend to include database storing the data,
and I suppose the Berkeley DB is really suitbale for my application.

EDIT: There are many improvment can be achieve, especially for the joinbase algorithm.
The join procedure can be modified to merege-join instead of the complete join in this
version. And there are some variable can be applied to more efficient data structure, 
such as the 'distance'.
