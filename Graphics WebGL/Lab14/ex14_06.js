// ���������� ����������
var container, camera, controls, scene, renderer, light; 
var objects = [ ];

// �������� �������� ����� ������ �������� ��������
window.onload = function()
{
init();
animate();
}

function init()
{
scene = new THREE.Scene(); //������� ����� 
AddCamera( 0, 200, 600); //��������� ������
AddLight( 0, 500, 500 ); //������������� ����� ����

//������� ��������
renderer = new THREE.WebGLRenderer( { antialias: true } ); 
renderer.setClearColor( 0xffffff );
renderer.setSize( window.innerWidth, window.innerHeight ); 
renderer.shadowMapEnabled = true;
container = document.getElementById('MyWebGLApp'); 
container.appendChild( renderer.domElement );

// ��������� �����
var Texture = new THREE.ImageUtils.loadTexture( 'checkerboard.jpg' );
Texture.wrapS = THREE.RepeatWrapping;
Texture.wrapT = THREE.RepeatWrapping;
Texture.repeat.set( 4, 4 );
Texture.offset.set( 0.5, 0 );
var Material = new THREE.MeshBasicMaterial( { map: Texture, side: THREE.DoubleSide } );
var Geometry = new THREE.PlaneGeometry(400, 400, 1, 1);
var checkerboard = new THREE.Mesh(Geometry, Material);
checkerboard.position.y = -8;
checkerboard.rotation.x = Math.PI / 2;
checkerboard.receiveShadow = true; 
checkerboard.castShadow = true;
scene.add(checkerboard);

// ������� ����� - ��������:
objects.push( checkerboard );
var geometry = new THREE.CylinderGeometry( 22, 22, 16, 36 ); 
var material = new THREE.MeshPhongMaterial( { color: 0x9b2d30, specular: 0x00b2fc, emissive: 0x000000, shininess: 40, shading: THREE.FlatShading,
blending: THREE.NormalBlending, depthTest: true } );
var x0 = -175; 
z0 = 175;
var key_count=12;
for ( var i = 0; i < key_count; i ++ )
{
var object = new THREE.Mesh( geometry, material );
if (i==4) { x0 = -175 - 7 * 50; z0 = 175 - 50; } 
if (i==8) { x0 = -175 - 16 * 50; z0 = 175 - 2*50; }
object.position.set( x0 + i * 2*50, 0, z0); 
scene.add( object ); 
objects.push( object );
}
      
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
light.castShadow = true;
scene.add( light );
}
