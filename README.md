Peer to Peer Video Streaming

This is our implementation of a Peer to Peer Video streaming system which is implemented in Java8 SE.

Here are some of the salient features of the Project.
 - Any client in the system will be able to request for a particular video or search the swarm for a list of available videos.
 - Peers may collect chunks of the video from a single peer or multiple peers.
 - The centralized server will coordinate registration of a new peer, deletion of any existing peer and also provide information about list of peers who have the video.
 - The videos are exchanged via a connection-less UDP Datagram.
 - The Client Server communication occurs over connection oriented TCP Sockets.
 - Since the system uses the VLC Media Player, it can play any video that VLC Supports.

Requirements:
 - Java 8 SE
 - VLC Media Player that can be initiated via the Terminal
 - Eclipse(Optional, To make code changes)

Operating Systems tested on:
 - Fedora 22 Linux
 - Microsoft Windows 10 (With relevant changes to the code base)

Instructions to Run:
 - Make sure that the videos to be shared are stored in the User.Home/Videos directory.
 - Make sure that the Server process is running by executing the Server.java / Server.class file.
 - Execute the Client.java / Client.class file.
