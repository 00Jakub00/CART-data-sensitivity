(async function () {
  const container = d3.select('#stromy');

  let files;
  try {
    files = await fetch('/list-json').then(r => r.json());
  } catch (e) {
    container.append('p').text('Chyba pri načítaní zoznamu súborov');
    console.error(e);
    return;
  }
  if (!files.length) {
    container.append('p').text('Žiadne JSON súbory nenájdené');
    return;
  }

  for (let i = 0; i < files.length; i++) {
    const name = files[i];
    let data;
    try {
      data = await fetch(`/stromy/${name}`).then(r => r.json());
    } catch (e) {
      console.error('Nepodarilo sa načítať', name, e);
      continue;
    }

    const wrap = container.append('div').attr('class', 'tree-container');
    const heading = i === 0
      ? 'Výsledný rozhodovací strom so zahrnutím všetkých atribútov.'
      : `Výsledný rozhodovací strom bez zahrnutia atribútu č. ${i - 1}`;
    wrap.append('h2').text(heading);

    const treeData = data.tree || data;
    const dx = 60, dy = 120;
    const root = d3.hierarchy(treeData);
    d3.tree()
      .nodeSize([dx, dy])
      .separation((a, b) => a.depth === b.depth ? 2 : 1)
      (root);

    const xs = root.descendants().map(d => d.x);
    const ys = root.descendants().map(d => d.y);
    const minX = d3.min(xs), maxX = d3.max(xs);
    const minY = d3.min(ys), maxY = d3.max(ys);
    const width = maxX - minX + dx * 2;
    const height = maxY - minY + dx * 2;

    const svg = wrap.append('svg')
      .attr('width', width)
      .attr('height', height)
      .style('display', 'block')
      .style('margin', '0 auto');

    const g = svg.append('g')
      .attr('transform', `translate(${dx - minX},${dx})`);

    g.selectAll('path.link')
      .data(root.links())
      .join('path')
      .attr('class', 'link')
      .attr('d', d3.linkVertical().x(d => d.x).y(d => d.y))
      .attr('stroke', '#444')
      .attr('stroke-width', 1.5)
      .attr('fill', 'none');

    g.selectAll('text.edge-label')
      .data(root.links())
      .join('text')
      .attr('class', 'edge-label')
      .attr('x', d => (d.source.x + d.target.x) / 2)
      .attr('y', d => (d.source.y + d.target.y) / 2 - 6)
      .attr('text-anchor', 'middle')
      .text(d => {
        const txt = d.source.data.text || '';
        const m = txt.match(/hodnota:\s*([\d.]+)/);
        if (m) {
          return d.target === d.source.children?.[0] ? '<' : '>=';
        } else {
          return d.target === d.source.children?.[0] ? '=' : '!=';
        }
      });

    const node = g.selectAll('g.node')
      .data(root.descendants())
      .join('g')
      .attr('class', 'node')
      .attr('transform', d => `translate(${d.x},${d.y})`);

    node.append('circle')
      .attr('r', 5)
      .attr('fill', '#4285f4');

    node.filter(d => d.data.text)
      .append('text')
      .attr('class', 'attr-text')
      .attr('dy', -12)
      .text(d => d.data.text.replace(
        /A č\.(\d+(\.\d+)?)/,
        m => `A č.${Math.round(parseFloat(m.slice(4)))}`
      ));

    node.filter(d => d.data.trieda)
      .append('text')
      .attr('class', 'class-text')
      .attr('dy', 20)
      .text(d => d.data.trieda);
  }
})();
