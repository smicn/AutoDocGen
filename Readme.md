# AutoDocGen
## A Java project with doc oprating, printer, GUIs and a reusable integration test tool
By Shaomin (Samuel) Zhang  smicn@foxmail.com <br>
April 2015 <br>

## Brief
Well, it is really nothing special about this Java project. <br>
It assumes that the input data preparation is ready, which is implemented simply by Java Serilizable methods;
and this software is to provide the data operating user interfaces using Java Swing. All after all, it is a 
simple J2SE project.
The reason that I share this project out to you is that it contains some code about my own understandings and
best practices about TDD (Test Driven Development). I would like demonstrate the Automatic Integration Testsute
in this project, which is easy to implement and deploy but much helpful during my routin software development. <br><br>
![](https://raw.githubusercontent.com/smicn/AutoDocGen/master/doc/AutoDocGen.gif) <br>

## About the Automatic Integration Testsuite
![](https://raw.githubusercontent.com/smicn/AutoDocGen/master/doc/Automatic_Integration_Testsuite.gif) <br>
If you are interested, the related source files are: <br>
* TestDriver.java <br>
* TestCase.java <br>
* CmdLine.java <br>
* TestDataSet.java (sample, but not reusable) <br>
* TestViever.java (GUI, not reusable) <br>
<br>

## Compilation and Execution
[1] How to compile? <br>
First of all, it is a J2SE project. Unzip this archive on any Win32 or Linux machine that support Java-1.6 
or above version. It is suggested that you choose Eclipse to import and open this project. NetBeans or other Java IDE should 
also be acceptable, but you may have to spend several minutes on project configuration. After being successfully imported, just 
try the usual way of chosen IDE to build the project. <br>
[2] How to execute? <br>
When you see the main window named "AutoDocGen...Welcome", please try to click the buttons on that menu. 
The main features of this software consist of three main parts: 
* a. Add/Delete/View customers information; <br> 
* b. View and print bill information; <br>
* c. Auto test suite. <br>
About the requirement specification and designs of this project, please refer to the project documents. <br>

## Contact me
E-mail: smicn@foxmail.com <br>
Linkedin: https://www.linkedin.com/in/shaomin-zhang-0ba60667
