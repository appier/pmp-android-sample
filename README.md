# PMP Android Sample

This is a sample app to demonstrate Appier Ads SDK and mediation to different networks.

## Development Guide

To use local developing SDK source, please create symbolic link to local `pmp-android-sdk` repo.

``` bash
rm -rf appier-sdk
ln -s ../pmp-android-sdk appier-sdk
```

To use local developing MoPub mediation source, please create symbolic link to local `pmp-mopub-android-mediation` repo's `appier-mediation` directory.

``` bash
rm -rf appier-mopub-mediation
ln -s ../pmp-mopub-android-mediation/appier-mediation appier-mopub-mediation
```

And don't forget to update gradle implementation.
