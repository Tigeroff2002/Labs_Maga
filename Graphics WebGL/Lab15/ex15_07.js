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
AddCamera( 2, 200, 700); //��������� ������
AddLight( 0, 0, 500 ); //������������� ����� ����

//������� ��������
renderer = new THREE.WebGLRenderer( { antialias: true, alpha: true } ); 
renderer.setPixelRatio(window.devicePixelRatio);
//renderer.setClearColor( 0x6f267d, 0 );
renderer.setSize( window.innerWidth, window.innerHeight ); 
container = document.getElementById('MyWebGLApp'); 
container.appendChild( renderer.domElement );

	var floorTexture = new THREE.ImageUtils.loadTexture( 'black.jpg' );
	floorTexture.wrapS = floorTexture.wrapT = THREE.RepeatWrapping; 
	floorTexture.repeat.set( 10, 10 );
	var floorMaterial = new THREE.MeshBasicMaterial( { map: floorTexture, side: THREE.DoubleSide } );
	var floorGeometry = new THREE.PlaneGeometry(10000, 10000, 10, 10);
	var floor = new THREE.Mesh(floorGeometry, floorMaterial);
	scene.add(floor);


   var terrainSize = terrain.length; //������ ����������� 100
            var geometry = new THREE.Geometry();
            var vertex, value, valueProb, distanceProbX, distanceProbY, i1, j1;
            for (var i = 0; i < terrainSize; i++) {
                for (var j = 0; j < terrainSize; j++) {
                    i1 = i - terrainSize / 2;
                    j1 = j - terrainSize / 2;

                    value = terrain[i][j];
                    valueProb = value / terrainSize;
                    distanceProbX = Math.abs(i1 / (terrainSize / 2));
                    distanceProbY = Math.abs(j1 / (terrainSize / 2));
                    if (Math.random() < valueProb && Math.random() > distanceProbX && Math.random() > distanceProbY) {
                        vertex = new THREE.Vector3();
                        vertex.x = (i - terrainSize / 2) * 8 + (Math.random() - 0.5) * 8;
                        vertex.y = value / 3;
                        vertex.z = (j - terrainSize / 2) * 8 + (Math.random() - 0.5) * 8;
                        geometry.vertices.push(vertex);
                    }
                }
            }

    // �������� ��������
    var textureLoader = new THREE.TextureLoader();
    var material = new THREE.PointsMaterial({
        color: 0xffdb8f,
        size: 10,
        map: textureLoader.load("particle.png"),
        blending: THREE.AdditiveBlending,
        transparent: true
    });
    // ��������� ������� ������ �� �����
    var particles = new THREE.Points(geometry, material);
    scene.add(particles);
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
light = new THREE.DirectionalLight( 0x000000 );
light.position.set(X,Y,Z);
scene.add( light );
}
