FCKConfig.AutoDetectLanguage	= true ;
FCKConfig.DefaultLanguage		= 'fr' ;

FCKConfig.ProcessHTMLEntities = false ;
FCKConfig.IncludeLatinEntities = false ;
FCKConfig.IncludeGreekEntities = false ;

FCKConfig.ToolbarSets["Default"] = [
	['Source','DocProps','-','Save','NewPage','Preview','-'],
	['Cut','Copy','Paste','PasteText','PasteWord','-','Print'],
	['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
	['Form','Checkbox','Radio','TextField','Textarea','Select','Button','HiddenField'],
	'/',
	['Bold','Italic','Underline','StrikeThrough','-','Subscript','Superscript'],
	['OrderedList','UnorderedList','-','Outdent','Indent','Blockquote','CreateDiv'],
	['JustifyLeft','JustifyCenter','JustifyRight','JustifyFull'],
	['Link','Unlink','Anchor'],
	['Image','Flash','Table','Rule','Smiley','SpecialChar','PageBreak'],
	'/',
	['Style','FontFormat','FontName','FontSize'],
	['TextColor','BGColor'],
	['FitWindow','ShowBlocks','-','About']		// No comma for the last row.
] ;

FCKConfig.ToolbarSets["Basic"] = [
	['Bold','Italic','-','OrderedList','UnorderedList','-','Link','Unlink','-','About']
] ;

FCKConfig.SpellChecker			= 'WSC' ;	// 'WSC' | 'SCAYT' | 'SpellerPages' | 'ieSpell'
FCKConfig.IeSpellDownloadUrl	= 'http://www.iespell.com/download.php' ;
FCKConfig.SpellerPagesServerScript = 'server-scripts/spellchecker.jsp' ;	// Available extension: .php .cfm .pl
FCKConfig.FirefoxSpellChecker	= true ;

FCKConfig.LinkBrowser = false ;

FCKConfig.ImageBrowser = false ;

FCKConfig.FlashBrowser = false ;

FCKConfig.LinkUpload = false ;

FCKConfig.ImageUpload = false ;

FCKConfig.FlashUpload = false ;
