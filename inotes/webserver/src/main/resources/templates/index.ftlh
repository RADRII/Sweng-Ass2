<!DOCTYPE html>
<html>
<head><title></title>
    <link rel="stylesheet" href="tachyons.min.css">
</head>
<body class="ph3 pt0 pb4 mw7 center sans-serif">
<h1 class="f2 mb0"><span class="gold">Simple</span>Calculator</h1>
<p class="f5 mt1 mb4 lh-copy">A simple spring boot calculator app.</p>
<form action="/calculate" method="POST" enctype="multipart/form-data">
    <ol class="list pl0">
        <li class="mv3"><label class="f6 b db mb2" for="description">Enter your expression here</label>
            <textarea class="f4 db border-box hover-black w-100 measure ba b--black-20 pa2 br2 mb2" rows="5"
                      name="description"><#if description??>${description}</#if></textarea>
            <input class="f6 link dim br1 ba bw1 ph3 pv2 mb2 dib black bg-white pointer" type="submit" value="Calculate!"
                   name="add">
        </li>
    </ol>
</form>
<ul class="list pl0"><p class="f6 b db mb2">Result</p>
    <li class="mv3 bb bw2 b--light-yellow bg-washed-yellow ph4 pv2"><p class="measure"></p>
        <#if answer??>
            <p>${answer}</p>
            <p></p>
        <#else>
            <#if expression??>
                <p>${expression}</p>
            <#else>
                <p>answer null</p>
            </#if>
        </#if>
    </li>
</ul>
</body>
</html>