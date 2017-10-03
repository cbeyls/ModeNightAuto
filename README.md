# AppCompat's broken MODE_NIGHT_AUTO demonstration

A sample project to demonstrate that ```MODE_NIGHT_AUTO``` does not work as expected in the support library. This is not a regression, this issue has always been present.

## About the issue

When a day/night time change occurs in an application using AppCompat's DayNight themes with ```MODE_NIGHT_AUTO```, only the top activity of the application will change theme immediately. When navigating back to other activities in the back stack, those retain the old theme.

This can be noticed in all Android versions (API 14 to 26).

That's because the manual ```Configuration``` change is global to the app and the method ```AppCompatDelegate``` uses to detect if an activity needs to be recreated is to compare the current ```Configuration``` flags to the expected mode (day or night):

```
(from AppCompatDelegateImplV14.java)

        final Resources res = mContext.getResources();
        final Configuration conf = res.getConfiguration();
        final int currentNightMode = conf.uiMode & Configuration.UI_MODE_NIGHT_MASK;

        final int newNightMode = (mode == MODE_NIGHT_YES)
                ? Configuration.UI_MODE_NIGHT_YES
                : Configuration.UI_MODE_NIGHT_NO;

        if (currentNightMode != newNightMode) {
                ...
        }
```

The top activity correctly detects a mismatch in ```onStart()``` and recreates itself, updating the ```Configuration``` in the process.
But then, when the user navigates back to the previous activity, it doesn't detect any mismatch in ```onStart()``` anymore because the global ```Configuration``` already reflects the expected mode. Yet, the existing view hierarchy of that activity still uses the incorrect theme.

## Steps to reproduce

- Launch this project on any device
- From main activity, press the button to launch the secondary activity.
- Switch to the phone settings app and change the current time to night if we are day (or day if we are night).
- Switch back to the app. Secondary activity changes its theme properly.
- Navigate back from secondary activity to primary activity. Notice the primary activity does *not* change theme.
