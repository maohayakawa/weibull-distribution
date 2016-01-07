use strict;
use warnings;
use utf8;
use DBI;
use DateTime;

#MySQLの情報
my $user = 'root';
my $passwd = '1108';
my $db = DBI->connect('DBI:mysql:recruit:localhost', $user, $passwd);
my $bunri = 2; #(1-文系 2-理系)
my $gakushu = 2; #(1-博士 2-修士 3-学部 4-短大 5-専門学校 6-高専)
my $sth = $db->prepare("SELECT last_date,gakko_name FROM all_retire_students WHERE bunri = '$bunri' and gakushu = '$gakushu'");
$sth->execute;
my $num_rows = $sth->rows;
my @school_name;
my @old_date;
my @date;
my @last_date;  
my @w_new; #重み
my @data;
my @row;
my $k = 3; #混合数
my @n = ();
my @pai =();

while(@row = $sth->fetchrow_array) {
  push @date => $row[0];
}
$sth->finish;
$db->disconnect;

=pod
my $gakko_name="早稲田大学";
foreach (@row_ary) {
  if($_ eq $gakko_name) {
    push @data => $_;
  }
  print "\$_ is $_\n";
}
=cut

my @new_date;
my @last_new_date;
date_change(@date);#時間変化のファンクション
foreach(@new_date) {
  if($_ > 0 ) {
    push @last_new_date => $_;
  }
}
my $length = @last_new_date; #データ数

#Step1 初期値の代入
for my $number (0..($length-1)) {
  if($last_new_date[$number] < 180) {
    $w_new[0][$number] = 0.9;
    $w_new[1][$number] = $w_new[2][$number] = 0.1;
  } elsif($last_new_date[$number] >= 180 and $last_new_date[$number] < 300) {
    $w_new[1][$number] = 0.9;
    $w_new[0][$number] = $w_new[2][$number] = 0.1;
  }else {
    $w_new[2][$number] = 0.9;
    $w_new[0][$number] = $w_new[1][$number] = 0.1;
  }
}
=pod;
my $seed = 12345;
for my $data_count (0..$length-1) {
  my $ransu = 1;
  srand $seed;
  for my $class_count (0..$k-2) {
    $w_new[$class_count][$data_count] = rand($ransu);
    $ransu -= $w_new[$class_count][$data_count];
  }
  $w_new[$k-1][$data_count] = $ransu;
  $seed = rand int(10000);
}
=cut
my $loop =10000;
my $epsilon = 1e-7;
my $error = 'fales';
my @w;
for my $loop_count (0..$loop){
  print "$loop_count 回目\n";
  @w = ();
  for my $ex (@w_new) {
    push @w => $ex;
  }
  #print "\@w[0] = @{$w[0]}\n";  
  #my @w <= @w_new;
  #Step1-2 各クラスのデータ数の個数,混合比
  my $sum_n;
  for my $cluss_number (0..($k-1)) {
    $n[$cluss_number] = 0;
    for my $data_number (0..($length-1)) {
      $n[$cluss_number] += $w[$cluss_number][$data_number];
    }
    $sum_n +=  $n[$cluss_number];
    push @pai => ($n[$cluss_number]/$length);
  }
  #print "\@n = @n\n";
  #print "sum_n = $sum_n\n";

#Step2 ワイブル分布のパラメータ計算
  my @scale;
  my @shape;
  for my $cluss_number(0..$k-1){
    my ($scale,$shape) = weibull_paramater(@{$w[$cluss_number]},$n[$cluss_number],@last_new_date);
    print "scale = $scale ,, shape = $shape\n";
    push @scale => $scale;
    push @shape => $shape;
  }
#Step3 重みの更新
  for my $data_count(0..$length-1){
    my $mother;
    my $w_sum;
    for my $class_count2(0..$k-1) {
      my $probability = weibull($scale[$class_count2],$shape[$class_count2],$last_new_date[$data_count]);
      $mother += $probability;
    }
    for my $class_count(0..$k-1) {
      my $child = weibull($scale[$class_count],$shape[$class_count],$last_new_date[$data_count]);
      $w_new[$class_count][$data_count] = $child/$mother;
      #print "w_new[$class_count][$data_count] = $w_new[$class_count][$data_count]\n";
      $w_sum += $w_new[$class_count][$data_count];
    }
    #print "w_sum = $w_sum\n";
  }
  for my $data_count (0..$length-1){
    for my $class_count (0..$k-1) {
      unless (abs($w[$class_count][$data_count]-$w_new[$class_count][$data_count]) < $epsilon) {
        $error = 'true';
      }
    }
  }
  if($error eq 'false'){
    #print "shape= @shape,,scale=@scale\n";
    last
  }
}

#print "shape=@shape,,scale = @scale";


#時間変換のファンクション
sub date_change {
  my @array = @_;
  my $dt2;
  my $dt1 = DateTime -> new(
    year => 2011,
    month => 12,
    day => 1,
  );
  foreach(@array) {
    if(/(\S+)-(\S+)-(\S+)/){
      #print "years $1, month $2, day $3\n";
      $dt2 = DateTime -> new(
        year => $1,
        month => $2,
        day => $3,
      );
    }
    my $duration = $dt2 - $dt1;
    my @units = $duration->in_units( qw(years months days) );
    my $date_clu = $units[0]*365+$units[1]*30+$units[2];
    push @new_date => $date_clu;
  }
}

#ワイブル分布のパラメータ計算ファンクションン
sub weibull_paramater {
  my @wait=();
  for(0..$length-1) {
    my $ex = shift;
    push @wait => $ex;
  }
  my $n_count = shift;
  #print "The number of data = $n_count\n";
  my $wait_length = @wait;
  #print "\@wait = @wait\n";
  #print "waitlength = $wait_length\n";
  my @array = @_;
  my $data_length = @array;
  #print "\@array = @array\n";
  #print "datalength = $data_length\n";
  my @scale;
  my @shape;
  my $loop = 10000;
  my $epsilon = 1e-7;
  my $error = "true";
  my $a = 1;
  my $m = 1;
  for my $loop_count (0..$loop) {
    my $a_before = $a;
    my $m_before = $m;
    my $sum_temp1 = 0;
    my $sum_temp2 = 0;
    my $sum_temp1_2 = 0;
    for my $data_count (0..$length-1) {
      my $temp1 = ($array[$data_count]**$m_before)*$wait[$data_count];
      my $temp2 = log($array[$data_count])*$wait[$data_count];
      #print "temp1 = $temp1\n";
      #print "temp2 = $temp2\n";
      $sum_temp1 += $temp1;
      $sum_temp2 += $temp2;
      $sum_temp1_2 += ($temp1)*($temp2);
    }
    $a = $length/$sum_temp1;
    $m = $length/($a*$sum_temp1_2-$sum_temp2);
    #print "\$sum_temp1 = $sum_temp1\n";
    #print "\$a = $a\n";
    #print "\$a_before = $a_before\n";
    #print "\$m = $m\n";
    #print "\$m_before = $m_before\n";
    #print "\$sum_temp1_2 = $sum_temp1_2\n";
    #print "\$sum_temp2 = $sum_temp2\n";

    if(abs($a-$a_before) < $epsilon and abs($m-$m_before) < $epsilon) {
      $error = "false";
      last;
    }
  }
  if($error eq "true"){
    print "収束しませんでした\n";
    return undef;
  }
  my $scale = ((1/$a)**(1/$m));
  my $shape = $m;
  return $scale,$shape;
}

#ワイブル分布の確率密度関数ファンクション
sub weibull {
  my $scale = shift;
  my $shape = shift;
  my $data = @_;

  my $probability = ($shape/$scale)*(($data/$scale)**$shape-1)*exp(-1*(($data/$scale)**$shape));
  return $probability;
}


