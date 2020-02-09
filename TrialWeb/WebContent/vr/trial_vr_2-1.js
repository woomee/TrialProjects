var container;
var camera, scene, renderer, element, controls;
var group;
var baseColor=0xffffff;

$(function() {
  if ( ! Detector.webgl ){
    Detector.addGetWebGLMessage();
  }else{
    init();
    animate();
  }
});

function init(){
  scene = new THREE.Scene();
  container = document.getElementById('world');

  camera = new THREE.PerspectiveCamera( 90, window.innerWidth / window.innerHeight, 1, 10000 );
  camera.position.z = 2000;

  renderer = new THREE.WebGLRenderer({ alpha: true });
  renderer.setPixelRatio( window.devicePixelRatio );
  renderer.setClearColor(baseColor);
  container.appendChild( renderer.domElement );

  //Light
  ambLight = new THREE.AmbientLight( 0xffffff, 0.5 );
  scene.add( ambLight );
  //
  dirLight = new THREE.DirectionalLight( 0xffffff, 1 );
  dirLight.position.set( 1,1,1 );
  scene.add( dirLight );

  //マウス操作
  controls = new THREE.OrbitControls(camera);
  // [umino] このAPIはTheee.js r72には無い
  //controls.rotateUp(Math.PI / 4);
  controls.target.set(
    camera.position.x + 0.1,
    camera.position.y,
    camera.position.z + 1
  );
  controls.noZoom = true;
  controls.noPan = true;

  //ジャイロに切り替え
  function setOrientationControls(e) {
    log('alpha:' + e.alpha);
    log('beta:' + e.beta);
    log('gamma:' + e.gamma);

    if (!e.alpha) {
      return;
    }
    controls = new THREE.DeviceOrientationControls(camera, true);
    controls.connect();
    controls.update();
    /* リスナは除去する　*/
    window.removeEventListener('deviceorientation', setOrientationControls, true);
    log ("Remove deviceorientation listener")
  }
 
//  window.addEventListener('deviceorientation', setOrientationControls, true);
  if( window.DeviceOrientationEvent ){
    //. iOS13 以上であれば DeviceOrientationEvent.requestPermission 関数が定義されているので、ここで条件分岐
    if( DeviceOrientationEvent.requestPermission && typeof DeviceOrientationEvent.requestPermission === 'function' ){
      //. iOS 13 以上の場合、
      //. ユーザーに「許可」を求めるダイアログを表示
      DeviceOrientationEvent.requestPermission().then( function( response ){
        log("ClickRequestDeviceSensor: Response: " + response);

        if( response === 'granted' ){
          //. 許可された場合のみイベントハンドラを追加できる
          window.addEventListener( 'deviceorientation', setOrientationControls, true );
        }
      });
    } else{
      //. Android または iOS 13 未満の場合、
      //. DeviceOrientationEvent オブジェクトが有効な場合のみ、deviceorientation イベント発生時に setOrientationControls 関数がハンドリングするよう登録
      window.addEventListener( "deviceorientation", setOrientationControls, true );
    }
  }
  log("Add deviceorientation listener");

  //両眼カメラレンダリング
  effect = new THREE.StereoEffect( renderer );
  //effect.eyeSeparation = 5;

  stats = new Stats();
  stats.domElement.style.position = 'absolute';
  stats.domElement.style.top = '0px';
  container.appendChild( stats.domElement );

  scene.fog = new THREE.Fog( baseColor, 500,cubeAreaSize );

  window.addEventListener( 'resize', resize, false );
  setTimeout(resize, 1);

  makeCubes();
}

function ClickRequestDeviceSensor(){
  log("ClickRequestDeviceSensor");

  //. ユーザーに「許可」を求めるダイアログを表示
  DeviceOrientationEvent.requestPermission().then( function( response ){
    log("ClickRequestDeviceSensor: Response: " + response);

    if( response === 'granted' ){
      //. 許可された場合のみイベントハンドラを追加できる
      window.addEventListener( 'deviceorientation', setOrientationControls, true );
      //. 画面上部のボタンを消す
      $('#sensorrequest').css( 'display', 'none' );
    }
  }).catch( function( e ){
    log( e );
  });
}

function log(msg) {
  var logDiv = document.getElementById("log");
  logDiv.innerHTML += (msg + "<br/>");
}


function resize() {
  var width = window.innerWidth;
  var height = window.innerHeight;
  camera.aspect = width / height;
  camera.updateProjectionMatrix();
  //renderer.setSize( width, height );
  effect.setSize( width, height );
}

function update(){
  cubeUpdate();
}
function render() {
  controls.update();
  //renderer.render( scene, camera );
  effect.render( scene, camera );
}
function animate() {

  requestAnimationFrame( animate );

//  update();
  render();
  stats.update();
}


//CUBE
var cubeColors=[
  0x3369e8,
  0xd50f25,
  0xeeb211,
  0x009925
];
var cubes=[],
cubeCnt=300,
cubeSizeBase=100,
cubeSizeRnd=300,
cubeAreaSize=12000;

function makeCubes(){
  group = new THREE.Group();
  scene.add( group );
  for(var i=0;i<cubeCnt;i++){
    var cube=new Cube();
    group.add(cube.obj);
    cubes.push(cube);
  }
}

function cubeUpdate(){
  for(var i=0; i<cubes.length; i++){
    cubes[i].update();
  }
}

var Cube=function(){
  var size=Math.random()*cubeSizeRnd+cubeSizeBase;
  this.geometry=new THREE.BoxGeometry( size, size, size );
  this.material=new THREE.MeshPhongMaterial({color:cubeColors[Math.floor(Math.random()*cubeColors.length)]});
  this.obj=new THREE.Mesh( this.geometry, this.material );
  this.obj.position.x=Math.random()*cubeAreaSize-cubeAreaSize/2;
  this.obj.position.y=Math.random()*cubeAreaSize-cubeAreaSize/2;
  this.obj.position.z=camera.position.z+(Math.random()*cubeAreaSize*2-cubeAreaSize);
  this.obj.rotation.x=Math.random()*2*Math.PI;
  this.obj.rotation.y=Math.random()*2*Math.PI;
  //this.obj.matrixAutoUpdate = false;
  //this.obj.updateMatrix();
  this.zSpeed=-(Math.random()*100+50);
  this.rSpeedX=Math.random()*0.2-0.1;
  this.rSpeedY=Math.random()*0.2-0.1;
};
Cube.prototype={
  update:function(){
    if(this.obj.position.z<camera.position.z-cubeAreaSize-1000){
      this.obj.position.z=camera.position.z+cubeAreaSize;
      this.obj.position.x=Math.random()*cubeAreaSize-cubeAreaSize/2;
      this.obj.position.y=Math.random()*cubeAreaSize-cubeAreaSize/2;
    }
    this.obj.position.z+=this.zSpeed;
    this.obj.rotation.x+=this.rSpeedX;
    this.obj.rotation.y+=this.rSpeedY;
  }
};


