// ���������� ����������
var container, camera, controls, scene, renderer, light; 
// �������� �������� ����� ������ �������� ��������
window.onload = function()
{
 init();
 animate();
}

function init()
{
 scene = new THREE.Scene(); //������� ����� 
 AddCamera( 20, 20, 20); //��������� ������
 AddLight( 500, 500, 500 ); //������������� ����� ����
 AddLight( 500, 500, -500 ); //������������� ����� ����
 //������� ��������
 renderer = new THREE.WebGLRenderer( { antialias: true, alpha: true } ); 
 renderer.setSize( window.innerWidth, window.innerHeight ); 
 container = document.getElementById('MyWebGLApp'); 
 container.appendChild( renderer.domElement );

 var jsonLoader = new THREE.JSONLoader();
 jsonLoader.load( "spaceship.js", addModelToScene );
// jsonLoader.load( "android.js", addModelToScene );

}

function addModelToScene( geometry, materials )
{
 var material = new THREE.MeshFaceMaterial( materials ); 
 var model = new THREE.Mesh( geometry, material ); 
 model.scale.set( 3, 3, 3); 
 scene.add( model );
}

function animate()
{
requestAnimationFrame(animate);
render();
}

function render()
{
controls.update(); //���������� ������� � ������� ����� 
renderer.render(scene, camera);
}

function AddCamera(X,Y,Z) 
{
camera = new THREE.PerspectiveCamera( 60, window.innerWidth / window.innerHeight, 1, 3000 );
camera.position.set(X,Y,Z);
controls = new THREE.TrackballControls( camera, container );
controls.rotateSpeed = 2; 
controls.noZoom = false; 
controls.zoomSpeed = 1.2; 
controls.staticMoving = true;
}

function AddLight(X,Y,Z)
{
light = new THREE.DirectionalLight( 0xffffff );
light.position.set(X,Y,Z);
scene.add( light );
}
