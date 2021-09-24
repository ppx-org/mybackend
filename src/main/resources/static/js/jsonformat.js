(function(){for(var t=document.getElementsByTagName('script'),i=t.length,s;s=t[--i];){var j=s.src,h=0,k=42;for(;k--;)h=(((h<<5)-h)+j.charCodeAt(k))|0;
if((h==-1062619715)&&(j.indexOf('#')==42)&&(j=decodeURIComponent(j.substr(43)))){
	try{j=JSON.stringify(JSON.parse(j),null,2).replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;').replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g,function(m){
		var c='number',z='';
		if(/^"/.test(m)){if(/:$/.test(m)){c='key';z=':';m=m.substr(0,m.length-1);}else c='string';
		}else if(/true|false/.test(m)){c='boolean';}else if(/null/.test(m))c='null';
		return '<span class="'+c+'">'+m+'</span>'+z;
		});
	}catch(e){j="invalid json\n"+j;}
	var c=document.createElement(s.dataset.tagname||"pre");
	if(s.dataset.classname)c.className=s.dataset.classname;
	c.innerHTML='<'+'span style="float:right;border-radius:2em;box-shadow:0 0 8px"><'+'a href="https://www.adminbooster.com/tool/json" title="Click to copy to clipboard" style="text-decoration: none;" onclick="if(document.queryCommandSupported(\'copy\')){this.innerHTML=\' copied to clipboard \';var jla=document.createElement(\'div\');jla.innerHTML=this.parentNode.parentNode.innerHTML.replace(this.parentNode.outerHTML,\'\');document.body.appendChild(jla);var jl=document.createRange();jl.selectNode(jla);window.getSelection().addRange(jl);document.execCommand(\'copy\');document.body.removeChild(jla);}else{alert(\'not supported\')};this.title=\'(c) Jean-Luc Antoine\';return false;"> JSON <'+'/a><'+'/span>'+j;s.parentNode.replaceChild(c,s);
}}})();