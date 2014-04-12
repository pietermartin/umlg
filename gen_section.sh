#!/bin/sh

#thanks to vert.x for a sample

# $1: markdown file
# $2: html template file
# $3: output html file

# Run markdown on the .md
markdown $1 -x toc > manual1.tmp

# Add a root element so it's well formed xml
#echo "<div>" > top.tmp
#echo "</div>" > end.tmp
#cat top.tmp manual1.tmp end.tmp > manual2.tmp

# Run xsl transform on it to add class attributes and other tweaks needed
# for good presentation using Twitter Bootstrap
#xsltproc addstyles.xsl manual2.tmp > manual3.tmp

# Insert the generated context in the actual HTML page
sed '/<!-- GENERATED CONTENT -->/ {
r manual1.tmp
d
}' $2 | cat > manual2.tmp

sed '/<!-- GENERATED MENU -->/ {
r doc/generated-menu/getting_started_menu.html
d
}' manual2.tmp | cat > $4

#rm manual*.tmp
#rm top.tmp
#rm end.tmp
