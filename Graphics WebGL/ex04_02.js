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
AddCamera( 0, 300, 500); //��������� ������
AddLight( 0, 0, 500 ); //������������� ����� ����

//������� ��������
renderer = new THREE.WebGLRenderer( { antialias: true } ); 
renderer.setClearColor( 0xffffff );
renderer.setSize( window.innerWidth, window.innerHeight ); 
container = document.getElementById('MyWebGLApp'); 
container.appendChild( renderer.domElement );
var a = 40, b = 20, c=0;
// ��������������� �������� ������: b=4;
var geometry = new THREE.Geometry; 
var material = new THREE.LineBasicMaterial( { color: 0xcc0000 } );
// ��������� ��������� ���������
for (var t = 0; t<= 6.3; t+= 0.1)
{
var vec =new THREE.Vector3( a*Math.cos( t ), b*Math.sin( t ), c*t);
geometry.vertices.push( vec );
}
// ������� ����� � ��������� �� �����
var line = new THREE.Line( geometry, material ); 
scene.add( line );
}

function animate()
{
requestAnimationFrame(animate);
render();
}
 
function render()
{
controls.update(); 
renderer.render(scene, camera);
}

function AddCamera(X,Y,Z) 
{
camera = new THREE.PerspectiveCamera( 45, window.innerWidth / window.innerHeight, 1, 10000 );
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
