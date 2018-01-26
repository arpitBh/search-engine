<?php
header('Content-Type: text/html; charset=utf-8');
include 'SpellCorrector.php';
ini_set("memory_limit",-1);
$time_start = microtime(true); 
$limit = 10;
$query = isset($_REQUEST['q']) ? $_REQUEST['q'] : false;
$sel = isset($_REQUEST['Select']) ? $_REQUEST['Select'] : "Lucene";
$results = false;

if ($query)
{
  require_once('solr-php-client-master/Apache/Solr/Service.php');
  $solr = new Apache_Solr_Service('localhost', 8983, '/solr/myexample');
  if (get_magic_quotes_gpc() == 1)
  {
    $query = stripslashes($query);
  }
  try
  {
    if ($sel=="Lucene") 
    {
      $additionalParameters = array('q.op' =>'AND');
      $results = $solr->search($query, 0, $limit, $additionalParameters);
    }
    elseif ($sel=="PageRank") 
    {
      $additionalParameters = array('q.op' =>'AND','sort' => 'pageRankFile desc');
      $results = $solr->search($query, 0, $limit, $additionalParameters);
    }
  }
  catch (Exception $e)
  {
    die("<html><head><title>SEARCH EXCEPTION</title><body><pre>{$e->__toString()}</pre></body></html>");
  }
}
?>

<html>
  <head>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>Solr Client - Lucene/Pagerank</title>
    <style>
      ol
      {
        font-size: 16;
        font-family: "Helvetica";
      }
      li
      {
        margin-bottom: 10;
        padding-left: 12; 
        vertical-align: center;
      }
      form
      {
        font-family: "Helvetica";
      }
      p
      {
        color: #666666;
        font-family: "Helvetica";
      }
      #gogreen
      {
        color: #006400;
        text-decoration: none;
      }
      #top
      {
        color:#80bfff;
        font-size :40; 
        margin-left: 25%;
        margin-top: 5;
        margin-bottom: 0;
      }
    </style>

  <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <link rel="stylesheet" href="/resources/demos/style.css">
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
  <script language="Javascript">
  function xmlhttpPost(strURL) 
  {
    var xmlHttpReq = false;
    var self = this;
    if (window.XMLHttpRequest) 
    {
      self.xmlHttpReq = new XMLHttpRequest();
    }
    else if (window.ActiveXObject) 
    { 
      self.xmlHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
    }
    self.xmlHttpReq.open('POST', strURL, true);
    self.xmlHttpReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    self.xmlHttpReq.onreadystatechange = function() 
    {
      if (self.xmlHttpReq.readyState == 4) 
      {
        updatepage(self.xmlHttpReq.responseText,getquerystring());
      }
    }
    var params = getstandardargs().concat(getquerystring()[0]);
    var strData = params.join('&');
    self.xmlHttpReq.send(strData);
  }

  function getstandardargs() 
  {
    var params = ['wt=json','indent=on'];
    return params;
  }
  function getquerystring() 
  {
    var form = document.forms['f1'];
    var query = form.q.value;
    var last = query.split(" ").splice(-1);
    var qstr=[];
    qstr[0] = "q="+escape(last);
    qstr[1] = query;
    return qstr;
  }
  function updatepage(str, query)
  {

    var q=query[1];
    var last = q.split(" ").splice(-1);
    var lastIndex = q.lastIndexOf(" ");
    q = q.substring(0, lastIndex);
    var json=JSON.parse(str);
    var suggestions=[];
    for (var i=0;i<5;i++)
    suggestions[i] = q +" "+ json.suggest.suggest[last].suggestions[i].term;
    $( "#q" ).autocomplete({source: suggestions});
  }
</script>
  </head>
  <body>
    <form  accept-charset="utf-8" method="get" name="f1">
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="radio" name="Select" value="Lucene" > Lucene &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="radio" name="Select" value="PageRank"> Page Rank <br>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <input id="q" name="q" type="text"  onkeyup='xmlhttpPost("http://localhost:8983/solr/myexample/suggest?"); return false;' value="<?php echo htmlspecialchars($query, ENT_QUOTES, 'utf-8'); ?>"/>
      <button type="submit" class="btn btn-primary">
        <i class="fa fa-search"></i> Search
      </button>
    </form>

  <div style="margin-top: 0; height:90%;float:top;">

    <?php
    if ($results)
    {
      $map=array();
      $handle = fopen("CleanMAP.txt", "r");
      if ($handle)
      {
        while (($line = fgets($handle)) !== false) 
        {
          $parts=explode(" ", $line);
          $map[$parts[0]]= $parts[1];
        }   
      fclose($handle);
      }
      $total = (int) $results->response->numFound;
      $start = min(1, $total);
      $end = min($limit, $total);
    ?> 

    <div style= "font-family:Helvetica">
    <?php  
    if ($total==0)
    {
      $suggest="";
      $url="";
      $parts=explode(" ", $query);
      for ($i = 0; $i < count($parts); $i++)
      {
        $sc=SpellCorrector::correct($parts[$i]);
        $suggest .= $sc." ";
        $url.=$sc."+";
      }
      $url=substr($url, 0,-1);
      $time_end = microtime(true); 
      if (strcmp($suggest,$query)!=1)
      {
      echo "<div><font size='4'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Query Time: ".round(($time_end-$time_start),2)." seconds."."&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;No results found. Did you mean <a href = http://localhost/First.php?Select=".$sel."&q=".$url."><i>".$suggest."</i></a> ?</font></div>";
      }
      else
      {
      echo "<div><font size='4'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Query Time: ".round(($time_end-$time_start),2)." seconds."."&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;No results found. </font></div>";
      }
    }
  else
  {
    $time_end = microtime(true); 
    echo "<p><i>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Query Time: ".round(($time_end-$time_start),2)." seconds."."&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Displaying results ".$start." - ".$end. " of ".$total.":</i></p>";
  }
  ?>
    </div>
    <ol>
      <?php
      foreach ($results->response->docs as $doc)
      {
        $path=explode("/",$doc->id);
        $f=array_slice($path, -1)[0];
        $url=$map[$f];
        $try = $doc->description;
        if (stripos($try, $query) !== false) {
          $pos = stripos($try, $query);
          if ($pos>=70)
          {
            $snip = "...".substr($try, $pos-70, $pos+70)."...";
          }
          elseif (strlen($try)>140)
            $snip = substr($try, 0, 140)."...";
          else
            $snip= $try;
          }
        else
        {
        $t = substr($try,0,140);
         $snip = ($doc->title);
         $pos = strripos($snip, "-");
         $snip = substr($snip, 0,$pos);
         $snip= $t." ... ".$snip." ...";
      }
      ?> 
      <li style="margin-bottom: 10.5; margin-left:17; ">
        <font size="4"><b><a target="_blank" href="<?php echo $url;?>"><?php echo($doc->title);?></a></b><br></font> 
        <font size="3"><?php echo $snip;?><br></font> 
        <font size="2">URL: <a  id = "gogreen" href="<?php echo $url;?>"><?php echo($url);?></a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ID: <?php echo($doc->id);?><br></font>
      </li>
    <?php
      }
    ?>
    </ol>
    <?php
    }
    ?>
  </div>
  </body>
</html>