# Droidster Utility

## How to use this library

>Step 1.Add it in your root build.gradle at the end of repositories:


	allprojects {

	repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
>Step 2. Add the dependency:
  
  	dependencies {
	      compile 'com.github.droidstered:DroidsterUtilityDemo:1.0'
	}
	
>Step 3. All Functions Name:

	1) customLogV
	
		Use : Util.customLogV("tag","print message");
		
	2) customToast
	
		Use : Util.customToast(this,"print message");
	
	3) getFormatedDate
	
		Use : Util.getFormatedDate(secondinlong,"dd MMM yyyy hh:mm");




