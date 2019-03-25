# toop-interface

A Java 8+ library providing the fixed interfaces to be used by national DC and DP implementations.

Latest release: **0.10.0** (2019-03) using data model 1.4.0

# Usage

This library contains two servlets occupying the paths `/to-dp` and `/to-dc`.
* `/to-dp` is used on the Data Publisher (DP) side only, for retrieving incoming messages from the TOOP Connector (TC). The TC expects this interface when forwarding stuff to the DP. Note: the interface was **extended** in v0.10 to handle both "TOOP Requests" (good case) as well as "TOOP Responses" (in case of error).
* `/to-dc` is used on the Data Consumer (DC) side only, for retrieving incoming messages from the TC. This interface only accepts TOOP responses.

Custom callback handlers for these servlets MUST be registered so that the messages are handled. Use `ToopInterfaceManager.setInterfaceDC` and `ToopInterfaceManager.setInterfaceDP` to set these callbacks. Do this only once globally, upon application startup. 

# Compile

```
mvn clean install
```

# Download

Binary releases (per version) can be downloaded from:
* http://repo2.maven.org/maven2/eu/toop/toop-interface/
    
Source code for the different versions is available at:
* https://github.com/TOOP4EU/toop-interface/releases/
    