###################
Web Services Facade
###################

Welcome to the Web Services Facade project, an OWS 9 testbed project.

The wsfacade is a service interpreting proxy for translating SOAP calls into WFS calls and back again.

Details
=======

Project Leads: `Jim Groffen <https://github.com/jgroffen>`_, `John Hudson <https://github.com/jhudson>`_

Source files use the following header::
   
 /*
 * Copyright 2012 LISAsoft - lisasoft.com. 
 * All rights reserved.
 *
 * This file is part of Web Services Facade.
 *
 * Web Services Facade is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Web Services Facade is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Web Services Facade.  If not, see <http://www.gnu.org/licenses/>.
 */ 
 
As indicated above the code is distributed under an `GPL 3 <https://github.com/lisasoft/wsfacade/blob/master/LICENSE>`_ license.

Build
-----

The Web Service Facade is build with modules:

 core - contains the core service, the 'platform'
 test-harness - is a convenience module to help test the service 

wsfacade is built using Maven::
  
  mvn clean install -Pall to build all of the modules
  mvn clean install -Pcore builds just the core service into a deployable WAR

Additional build profiles are documented in the root `pom.xml`:pom.xml ::
  
  more pom.xml

Test
----

Change directory to the test harness and run:

 mvn clean jetty:run-exploded
 
This will run a local version of the software on port 8080 which can be accessed at http://localhost:8080/wsfacade-testharness

Continious Integration
======================

Build Status: .. image:: https://travis-ci.org/lisasoft/wsfacade.png

Participation
=============

The project is hosted on github:

* https://github.com/lisasoft/wsfacade

Participation is encouraged using the github *fork* and *pull request* workflow::

* file headers are described above
* include test case demonstrating functionality
* contributions are expected to pass test and not break the build

Additional resources:

* https://github.com/lisasoft/wsfacade/issues