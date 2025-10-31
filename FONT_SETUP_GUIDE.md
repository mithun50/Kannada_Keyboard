# Kannada Font Setup Guide

## 📥 Download Kannada Fonts

You need to download 4 Kannada font files (.ttf format) and place them in the `app/src/main/res/font/` directory.

### Recommended Free Kannada Fonts

#### Option 1: Google Fonts (Recommended)
1. **Noto Sans Kannada Regular**
   - URL: https://fonts.google.com/noto/specimen/Noto+Sans+Kannada
   - Download and rename to: `kannada_regular.ttf`

2. **Noto Sans Kannada Bold**
   - Same URL, select Bold weight
   - Rename to: `kannada_bold.ttf`

3. **Noto Serif Kannada**
   - URL: https://fonts.google.com/noto/specimen/Noto+Serif+Kannada
   - Rename to: `kannada_serif.ttf`

#### Option 2: Other Free Sources
1. **Kedage Font**
   - URL: https://github.com/rkks/kannada-fonts
   - Rename to: `kannada_kedage.ttf`

2. **Lohit Kannada**
   - URL: https://releases.pagure.org/lohit/
   - Rename to: `kannada_lohit.ttf`

3. **Tunga Font** (Windows system font)
   - If on Windows: C:\Windows\Fonts\tunga.ttf
   - Rename to: `kannada_tunga.ttf`

## 📁 File Placement

After downloading, your structure should look like:

```
app/src/main/res/font/
├── kannada_regular.ttf      (Style 1 - Regular)
├── kannada_bold.ttf         (Style 2 - Bold)
├── kannada_serif.ttf        (Style 3 - Serif)
└── kannada_kedage.ttf       (Style 4 - Traditional)
```

## ⚠️ Important Notes

1. **File naming**: Use only lowercase and underscores (Android resource naming rules)
2. **File format**: Must be `.ttf` or `.otf`
3. **Font license**: Ensure fonts are free for commercial use
4. **File size**: Keep fonts under 500KB each for app size optimization

## 🔄 Alternative: Use System Fonts

If you don't want to bundle fonts, you can use Android system fonts:
- Default (no custom font)
- Serif
- Monospace
- Sans-serif

The implementation supports both bundled and system fonts.

## 🧪 Testing

After adding fonts:
1. Clean and rebuild project
2. Install on device
3. Open app and go to font selection
4. Test each font style on keyboard
5. Verify Kannada characters render correctly

## 📝 License Information

**Noto Fonts**: Open Font License (OFL) - Free for commercial use
**Kedage**: GPL - Free and open source
**Lohit**: GPL - Free and open source

Always verify license before distribution.
